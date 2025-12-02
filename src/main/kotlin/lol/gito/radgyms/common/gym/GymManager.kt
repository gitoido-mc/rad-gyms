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
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsEntities
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.state.PlayerData
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.util.delayExecute
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructureManager
import lol.gito.radgyms.common.util.displayClientMessage
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, Gym.Json> = mutableMapOf()

    fun register() = Unit

    fun initInstance(serverPlayer: ServerPlayer, serverWorld: ServerLevel, level: Int, type: String?): Boolean {
        RadGymsState.setReturnCoordsForPlayer(
            serverPlayer,
            PlayerData.ReturnCoords(
                serverWorld.dimension().location(),
                serverPlayer.position().toBlockPos()
            )
        )

        val startTime = markNow()
        val gymLevel = level.coerceIn(5..Cobblemon.config.maxPokemonLevel)

        // Check if we have gym dimension available
        val gymDimension = serverPlayer.getServer()?.getLevel(RadGymsDimensions.RADGYMS_LEVEL_KEY) ?: return false
        // Check if player not in gym dimension
        if (serverWorld.dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) return false

        // Get gym template name
        val gymType = when (type in GYM_TEMPLATES.keys) {
            true -> type
            else -> "default"
        }

        debug("Initializing $gymType template for $type gym")
        debug("Available templates ${GYM_TEMPLATES.keys}")

        val gym = GymTemplate.fromGymDto(serverPlayer, GYM_TEMPLATES[gymType]!!, gymLevel, type)
        val playerGymCoords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)
        val dest = BlockPos.containing(
            playerGymCoords.x + gym.relativePlayerSpawn.x,
            playerGymCoords.y + gym.relativePlayerSpawn.y,
            playerGymCoords.z + gym.relativePlayerSpawn.z
        )

        debug("Trying to place gym structure with ${gym.structure} at ${playerGymCoords.x} ${playerGymCoords.y} ${playerGymCoords.z} ")

        StructureManager.placeStructure(gymDimension, playerGymCoords, gym.structure)

        gymDimension.chunkSource.addRegionTicket(
            TicketType.PORTAL,
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
            modId("item.component.type.chaos").toLanguageKey()
        )

        val label = when (gymType) {
            "default" -> when (type) {
                "default" -> chaosTranslatable.string
                null -> chaosTranslatable.string
                else -> translatable(
                    cobblemonResource("type.${type}").toLanguageKey()
                ).string
            }

            null -> chaosTranslatable.string
            else -> translatable(cobblemonResource("type.${type}").toLanguageKey()).string
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
        gymDimension: ServerLevel,
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
        gymDimension: ServerLevel,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ): Pair<UUID, TrainerModel> {
        val trainerEntity = Trainer(RadGymsEntities.GYM_TRAINER, gymDimension)
            .apply {
                uuid = trainerUUID
                gymId = trainer.id
                leader = trainer.leader
                format = trainer.format.name
                trainerId = trainerUUID
                requires = requiredUUID
                yHeadRot = trainer.npc.yaw
                yBodyRot = trainer.npc.yaw
                customName = trainer.npc.name
                isCustomNameVisible = true
                setPersistenceRequired()
                setPos(
                    Vec3(
                        coords.x + trainer.npc.relativePosition.x,
                        coords.y + trainer.npc.relativePosition.y,
                        coords.z + trainer.npc.relativePosition.z
                    )
                )
            }.also {
                debug("Spawning trainer ${it.id} at ${it.x} ${it.y} ${it.z} in ${gymDimension.dimension()}")
                debug("Selected ${trainer.format} battle format for trainer")
                gymDimension.addFreshEntity(it)
            }

        return Pair(trainerEntity.uuid, trainer)
    }

    fun spawnExitBlock(gym: Gym) {
        val exitPos = BlockPos(
            (gym.coords.x + gym.template.relativeExitBlockSpawn.x).toInt(),
            (gym.coords.y + gym.template.relativeExitBlockSpawn.y).toInt(),
            (gym.coords.z + gym.template.relativeExitBlockSpawn.z).toInt(),
        )

        val world = server()!!.getLevel(RadGymsDimensions.RADGYMS_LEVEL_KEY)!!

        world.setBlockAndUpdate(
            exitPos,
            RadGymsBlocks.GYM_EXIT.defaultBlockState()
        )
    }

    fun handleGymLeave(serverPlayer: ServerPlayer) {
        val state = RadGymsState.getPlayerState(serverPlayer)
        var preloadPos: BlockPos
        var preloadDim: ServerLevel
        if (state.returnCoords != null) {
            preloadPos = state.returnCoords!!.position
            preloadDim = server()!!.getLevel(
                ResourceKey.create(
                    Registries.DIMENSION,
                    state.returnCoords!!.dimension
                )
            )!!
        } else {
            preloadDim = server()!!.getLevel(serverPlayer.respawnDimension)!!
            preloadPos = serverPlayer.respawnPosition ?: preloadDim.sharedSpawnPos
        }

        destructGym(serverPlayer)

        preloadDim.chunkSource.addRegionTicket(
            TicketType.PORTAL,
            preloadDim.getChunk(preloadPos).pos,
            2,
            preloadPos
        )
        serverPlayer.displayClientMessage(Component.nullToEmpty("5...") as MutableComponent)

        delayExecute(1f) {
            serverPlayer.displayClientMessage(Component.nullToEmpty("4...") as MutableComponent)
        }

        delayExecute(2f) {
            serverPlayer.displayClientMessage(Component.nullToEmpty("3...") as MutableComponent)
        }

        delayExecute(3f) {
            serverPlayer.displayClientMessage(Component.nullToEmpty("2...") as MutableComponent)
        }

        delayExecute(4f) {
            serverPlayer.displayClientMessage(Component.nullToEmpty("1...") as MutableComponent)
        }

        delayExecute(5f) {
            PlayerSpawnHelper.teleportPlayer(
                serverPlayer,
                preloadDim,
                preloadPos,
                yaw = serverPlayer.yRot,
                pitch = serverPlayer.xRot,
            ).also {
                debug("Gym instance removed from memory")
                RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)
            }
        }

        return
    }

    fun destructGym(serverPlayer: ServerPlayer, removeCoords: Boolean? = true) {
        if (!RadGymsState.hasGymForPlayer(serverPlayer)) return

        if (removeCoords == true) {
            RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)
        }

        val gym = RadGymsState.getGymForPlayer(serverPlayer)!!
        val world = serverPlayer.server.getLevel(RadGymsDimensions.RADGYMS_LEVEL_KEY)!!

        gym.npcList
            .forEach {
                debug("Removing trainer ${it.value} from registry and detaching associated entity")
                RCT.trainerRegistry.unregisterById(it.key.toString())
                world.getEntity(it.key)?.discard()
            }.also {
                RadGymsState.removeGymForPlayer(serverPlayer)
            }
    }
}
