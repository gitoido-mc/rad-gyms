/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.server
import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.api.dto.Gym
import lol.gito.radgyms.api.dto.TrainerModel
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.BlockRegistry
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.registry.EntityRegistry
import lol.gito.radgyms.common.state.PlayerData
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.util.delayExecute
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructureManager
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ChunkTicketType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, Gym.Json> = mutableMapOf()

    fun register() = Unit

    fun initInstance(serverPlayer: ServerPlayerEntity, serverWorld: ServerWorld, level: Int, type: String?): Boolean {
        val startTime = markNow()
        val gymLevel = level.coerceIn(5..Cobblemon.config.maxPokemonLevel)

        // Check if we have gym dimension available
        val gymDimension = serverPlayer.getServer()?.getWorld(DimensionRegistry.RADGYMS_LEVEL_KEY) ?: return false
        // Check if player not in gym dimension
        if (serverWorld.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) return false

        // Get gym template name
        val gymType = when (type in GYM_TEMPLATES.keys) {
            true -> type
            else -> "default"
        }

        debug("Initializing $gymType template for $type gym")
        debug("Available templates ${GYM_TEMPLATES.keys}")

        val gym = GymTemplate.fromGymDto(serverPlayer, GYM_TEMPLATES[gymType]!!, gymLevel, type)
        val playerGymCoords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)
        val dest = BlockPos.ofFloored(
            playerGymCoords.x + gym.relativePlayerSpawn.x,
            playerGymCoords.y + gym.relativePlayerSpawn.y,
            playerGymCoords.z + gym.relativePlayerSpawn.z
        )

        debug("Trying to place gym structure with ${gym.structure} at ${playerGymCoords.x} ${playerGymCoords.y} ${playerGymCoords.z} ")

        StructureManager.placeStructure(gymDimension, playerGymCoords, gym.structure)

        RadGymsState.setReturnCoordsForPlayer(
            serverPlayer,
            PlayerData.ReturnCoords(
                serverWorld.registryKey.value,
                serverPlayer.pos.toBlockPos()
            )
        )

        gymDimension.chunkManager.addTicket(
            ChunkTicketType.PORTAL,
            gymDimension.getChunk(dest).pos,
            4,
            dest
        )

        PlayerSpawnHelper.teleportPlayer(
            serverPlayer,
            gymDimension,
            dest,
            gym.playerYaw,
            0.0F
        )


        val trainerUUIDs = buildTrainers(gym, gymDimension, playerGymCoords)
        val chaosTranslatable = translatable(
            modId("item.component.type.chaos").toTranslationKey()
        )

        val label = when (gymType) {
            "default" -> when (type) {
                "default" -> chaosTranslatable.string
                null -> chaosTranslatable.string
                else -> translatable(
                    cobblemonResource("type.${type}").toTranslationKey()
                ).string
            }

            null -> chaosTranslatable.string
            else -> translatable(cobblemonResource("type.${type}").toTranslationKey()).string
        }

        val gymInstance = Gym(gym, trainerUUIDs, playerGymCoords, gymLevel, type ?: "default", label)

        RadGymsState.addGymForPlayer(serverPlayer, gymInstance)

        val trainerRegistry = RCT.trainerRegistry
        gymInstance.npcList.forEach { (uuid, npc) ->
            val trainer = trainerRegistry.registerNPC(
                uuid.toString(),
                npc.trainer
            )

            trainer.entity = gymDimension.getEntity(uuid) as Trainer
        }

        debug("Gym $gymType initialized, took ${startTime.elapsedNow().inWholeMilliseconds} ms")

        return true
    }

    private fun buildTrainers(
        template: GymTemplate,
        gymDimension: ServerWorld,
        coords: BlockPos
    ): Map<UUID, TrainerModel> {
        val trainerIds = mutableMapOf<String, Pair<UUID, TrainerModel>>()

        template.trainers.forEach {
            val uuid = UUID.randomUUID()
            val requiredUUID = when (it.requires) {
                null -> null
                else -> trainerIds[it.requires]?.first
            }

            buildTrainerEntity(it, gymDimension, coords, uuid, requiredUUID).let { entity ->
                trainerIds[it.id] = entity
            }
        }

        return trainerIds.map { it.value }.associate { it.first to it.second }
    }

    private fun buildTrainerEntity(
        trainer: TrainerModel,
        gymDimension: ServerWorld,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ): Pair<UUID, TrainerModel> {
        val trainerEntity = Trainer(EntityRegistry.GYM_TRAINER, gymDimension)
            .apply {
                uuid = trainerUUID
                gymId = trainer.id
                leader = trainer.leader
                format = trainer.format.name
                trainerId = trainerUUID
                requires = requiredUUID
                headYaw = trainer.npc.yaw
                bodyYaw = trainer.npc.yaw
                customName = Text.of(trainer.npc.name)
                isCustomNameVisible = true
                setPersistent()
                setPosition(
                    Vec3d(
                        coords.x + trainer.npc.relativePosition.x,
                        coords.y + trainer.npc.relativePosition.y,
                        coords.z + trainer.npc.relativePosition.z
                    )
                )
            }.also {
                debug("Spawning trainer ${it.id} at ${it.pos.x} ${it.pos.y} ${it.pos.z} in ${gymDimension.registryKey.value}")
                debug("Selected ${trainer.format} battle format for trainer")
                gymDimension.spawnEntity(it)
            }

        return Pair(trainerEntity.uuid, trainer)
    }

    fun spawnExitBlock(serverPlayer: ServerPlayerEntity) {
        if (!RadGymsState.hasGymForPlayer(serverPlayer)) return

        val gym = RadGymsState.getGymForPlayer(serverPlayer)!!

        val exitPos = BlockPos(
            (gym.coords.x + gym.template.relativeExitBlockSpawn.x).toInt(),
            (gym.coords.y + gym.template.relativeExitBlockSpawn.y).toInt(),
            (gym.coords.z + gym.template.relativeExitBlockSpawn.z).toInt(),
        )

        val world = server()!!.getWorld(DimensionRegistry.RADGYMS_LEVEL_KEY)!!

        world.setBlockState(
            exitPos,
            BlockRegistry.GYM_EXIT.defaultState
        )
        world.markDirty(exitPos)
    }

    fun handleGymLeave(serverPlayer: ServerPlayerEntity) {
        val state = RadGymsState.getPlayerState(serverPlayer)
        var preloadPos: BlockPos
        var preloadDim: ServerWorld
        if (state.returnCoords != null) {
            preloadPos = state.returnCoords!!.position
            preloadDim = server()!!.getWorld(
                RegistryKey.of(
                    RegistryKeys.WORLD,
                    state.returnCoords!!.dimension
                )
            )!!
        } else {
            preloadDim = server()!!.getWorld(serverPlayer.spawnPointDimension)!!
            preloadPos = serverPlayer.spawnPointPosition ?: preloadDim.spawnPos
        }

        destructGym(serverPlayer)

        preloadDim.chunkManager.addTicket(
            ChunkTicketType.PORTAL,
            preloadDim.getChunk(preloadPos).pos,
            2,
            preloadPos
        )
        serverPlayer.sendMessage(Text.of("5..."), true)

        delayExecute(1f) {
            serverPlayer.sendMessage(Text.of("4..."), true)
        }

        delayExecute(2f) {
            serverPlayer.sendMessage(Text.of("3..."), true)
        }

        delayExecute(3f) {
            serverPlayer.sendMessage(Text.of("2..."), true)
        }

        delayExecute(4f) {
            serverPlayer.sendMessage(Text.of("1..."), true)
        }

        delayExecute(5f) {
            PlayerSpawnHelper.teleportPlayer(
                serverPlayer,
                preloadDim,
                preloadPos,
                yaw = serverPlayer.yaw,
                pitch = serverPlayer.pitch,
            ).also {
                debug("Gym instance removed from memory")
                RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)
            }
        }

        return
    }

    fun destructGym(serverPlayer: ServerPlayerEntity, removeCoords: Boolean? = true) {
        if (!RadGymsState.hasGymForPlayer(serverPlayer)) return

        val gym = RadGymsState.getGymForPlayer(serverPlayer)!!
        val world = serverPlayer.server.getWorld(DimensionRegistry.RADGYMS_LEVEL_KEY)!!

        gym.npcList.forEach {
            debug("Removing trainer ${it.value} from registry and detaching associated entity")
            RCT.trainerRegistry.unregisterById(it.key.toString())
            world.getEntity(it.key)?.discard()
        }

        if (removeCoords == true) {
            RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)
        }
        RadGymsState.removeGymForPlayer(serverPlayer)
    }
}
