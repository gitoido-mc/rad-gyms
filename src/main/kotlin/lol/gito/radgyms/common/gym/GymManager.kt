/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.scheduling.ScheduledTask
import com.cobblemon.mod.common.api.scheduling.ServerTaskTracker
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.server
import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.common.RadGyms.LOGGER
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.EntityManager
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.BlockRegistry
import lol.gito.radgyms.common.registry.DataComponentRegistry
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import lol.gito.radgyms.common.world.StructureManager
import lol.gito.radgyms.server.state.PlayerData
import lol.gito.radgyms.server.state.RadGymsState
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.BundleContentsComponent
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ChunkTicketType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

data class GymInstance(
    val template: GymTemplate,
    val npcList: Map<UUID, GymTrainer>,
    val coords: BlockPos,
    val level: Int,
    val type: String
)

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, GymDTO> = mutableMapOf()
    val PLAYER_GYMS: MutableMap<UUID, GymInstance> = mutableMapOf()

    fun initInstance(serverPlayer: ServerPlayerEntity, serverWorld: ServerWorld, level: Int, type: String?): Boolean {
        PLAYER_GYMS.remove(serverPlayer.uuid)
        val startTime = markNow()
        val gymLevel = level.coerceIn(5..100)
        val gymDimension = serverPlayer.getServer()?.getWorld(DimensionRegistry.RADGYMS_LEVEL_KEY) ?: return false

        if (serverWorld.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) return false

        val pos = serverPlayer.pos
        val gymType = if (type in GYM_TEMPLATES.keys) type else "default"
        debug("Initializing $gymType template for $type gym")
        debug("Available templates ${GYM_TEMPLATES.keys}")

        val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }
        if (gym == null) {
            LOGGER.warn("Gym $gymType could not be initialized, no such type in template registry")
            return false
        }

        val coords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)
        val dest = BlockPos.ofFloored(
            coords.x + gym.relativePlayerSpawn.x,
            coords.y + gym.relativePlayerSpawn.y,
            coords.z + gym.relativePlayerSpawn.z
        )

        debug("Trying to place gym structure with ${gym.structure} at ${coords.x} ${coords.y} ${coords.z} ")
            .also { StructureManager.placeStructure(gymDimension, coords, gym.structure) }
            .also { debug("return dim ${serverWorld.registryKey.value}") }
            .also {
                RadGymsState.setReturnCoordsForPlayer(
                    serverPlayer,
                    PlayerData.ReturnCoords(
                        serverWorld.registryKey.value,
                        pos.toBlockPos()
                    )
                )
            }
            .also {
                PlayerSpawnHelper.teleportPlayer(
                    serverPlayer,
                    gymDimension,
                    dest,
                    gym.playerYaw,
                    0.0F
                )
            }
            .also {
                val trainerUUIDs = buildTrainers(gym, gymDimension, coords)
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
                PLAYER_GYMS[serverPlayer.uuid] = GymInstance(gym, trainerUUIDs, coords, gymLevel, label)
                debug("Gym $gymType initialized, took ${startTime.elapsedNow().inWholeMilliseconds}ms")
            }
        return true
    }

    private fun buildTrainers(
        template: GymTemplate,
        gymDimension: ServerWorld,
        coords: BlockPos
    ): Map<UUID, GymTrainer> {
        val trainerIds = mutableMapOf<String, Pair<UUID, GymTrainer>>()

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
        trainerTemplate: GymTrainer,
        gymDimension: ServerWorld,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ): Pair<UUID, GymTrainer> {
        val trainerEntity = Trainer(EntityManager.GYM_TRAINER, gymDimension)
            .apply {
                setPersistent()
                uuid = trainerUUID
                headYaw = trainerTemplate.npc.yaw
                bodyYaw = trainerTemplate.npc.yaw
                customName = Text.of(trainerTemplate.npc.name)
                isCustomNameVisible = true
                setPosition(
                    Vec3d(
                        coords.x + trainerTemplate.npc.relativePosition.x,
                        coords.y + trainerTemplate.npc.relativePosition.y,
                        coords.z + trainerTemplate.npc.relativePosition.z
                    )
                )
                trainerId = trainerUUID
                requires = requiredUUID
                leader = trainerTemplate.leader
            }.also {
                debug("Spawning trainer ${it.id} at ${it.pos.x} ${it.pos.y} ${it.pos.z} in ${gymDimension.registryKey.value}")
                gymDimension.spawnEntity(it)
            }

        return Pair(trainerEntity.uuid, trainerTemplate)
    }

    fun spawnExitBlock(serverPlayer: UUID) {
        val gym = PLAYER_GYMS[serverPlayer] ?: return

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
            1,
            preloadPos
        )

        ScheduledTask.Builder()
            .tracker(ServerTaskTracker)
            .delay(2f)
            .execute {
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
            .build()

        return
    }

    fun destructGym(serverPlayer: ServerPlayerEntity, removeCoords: Boolean? = true) {
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return

        val world = server()!!.getWorld(DimensionRegistry.RADGYMS_LEVEL_KEY)!!

        gym.npcList.forEach {
            debug("Removing trainer ${it.value} from registry and detaching associated entity")
            RCT.trainerRegistry.unregisterById(it.key.toString())
            world.getEntity(it.key)?.discard()
        }

        if (removeCoords == true) {
            RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)
        }
        PLAYER_GYMS.remove(serverPlayer.uuid)
    }

    fun handleLootDistribution(serverPlayer: ServerPlayerEntity, template: GymTemplate, level: Int, type: String) {
        val bundle = ItemStack(Items.BUNDLE)
        val bundleContents = BundleContentsComponent.Builder(BundleContentsComponent.DEFAULT)
        template
            .lootTables
            .filter {
                level in it.levels.first..it.levels.second
            }
            .forEach { table ->
                debug("Settling level $level rewards for player ${serverPlayer.name.literalString} after beating leader")
                val registryLootTable = serverPlayer
                    .server
                    .reloadableRegistries
                    .registryManager
                    .get(RegistryKeys.LOOT_TABLE)
                    .get(table.id) ?: return@forEach

                val lootContextParameterSet = LootContextParameterSet.Builder(serverPlayer.world as ServerWorld)
                    .add(LootContextParameters.THIS_ENTITY, serverPlayer)
                    .add(LootContextParameters.ORIGIN, serverPlayer.pos)
                    .build(LootContextTypes.GIFT)



                registryLootTable
                    .generateLoot(lootContextParameterSet)
                    .forEach { itemStack ->
                        bundleContents.add(itemStack)
                    }
            }

        val styledLevel = MutableText.of(Text.literal(level.toString()).content).formatted(Formatting.GOLD)
        val styledType = translatable(cobblemonResource("type.${type.lowercase()}").toTranslationKey())
            .setStyle(
                Style.EMPTY.withColor(Formatting.GREEN).withItalic(true)
            )

        bundle.set(
            DataComponentTypes.CUSTOM_NAME,
            translatable(
                modId("gym_reward").toTranslationKey("item"),
                styledLevel, styledType
            )
        )
        bundle.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContents.build())
        bundle.set(DataComponentRegistry.RAD_GYM_BUNDLE_COMPONENT, true)
        if (!serverPlayer.giveItemStack(bundle)) {
            ItemEntity(
                serverPlayer.world,
                serverPlayer.pos.x,
                serverPlayer.pos.y,
                serverPlayer.pos.z,
                bundle,
            ).let {
                serverPlayer.world.spawnEntity(it)
            }

        }
    }

    fun register() {
        debug("GymManager init")
    }
}
