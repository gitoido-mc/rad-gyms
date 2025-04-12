package lol.gito.radgyms.gym

import com.cobblemon.mod.common.api.scheduling.ScheduledTask
import com.cobblemon.mod.common.api.scheduling.ServerTaskTracker
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.entity.Trainer
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import lol.gito.radgyms.world.DimensionManager
import lol.gito.radgyms.world.PlayerSpawnHelper
import lol.gito.radgyms.world.StructureManager
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
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.text.TextColor
import net.minecraft.util.Colors
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.dimension.DimensionTypes
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

data class GymInstance(
    val template: GymTemplate,
    val npcList: List<Pair<UUID, GymTrainer>>,
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
        val gymDimension = serverPlayer.getServer()?.getWorld(DimensionManager.RADGYMS_LEVEL_KEY) ?: return false


        if (serverWorld.registryKey != DimensionManager.RADGYMS_LEVEL_KEY) {
            val pos = serverPlayer.pos
            val gymType = if (type in GYM_TEMPLATES.keys) type else "default"
            debug("Initializing $gymType template for $type gym")
            debug("Available templates ${GYM_TEMPLATES.keys}")

            val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }
            if (gym != null) {
                val coords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)

                val destX = coords.x + gym.relativePlayerSpawn.x
                val destY = coords.y + gym.relativePlayerSpawn.y
                val destZ = coords.z + gym.relativePlayerSpawn.z

                ScheduledTask.Builder()
                    .tracker(ServerTaskTracker)
                    .delay(0.1f)
                    .execute {
                        debug("Trying to place gym structure with ${gym.structure} at ${coords.x} ${coords.y} ${coords.z} ")
                        StructureManager.placeStructure(
                            gymDimension,
                            coords,
                            gym.structure
                        )
                    }
                    .build()

                ScheduledTask.Builder()
                    .tracker(ServerTaskTracker)
                    .execute {
                        debug("return dim ${serverWorld.registryKey.value}")
                        GymsNbtData.setReturnDimension(
                            serverPlayer as EntityDataSaver,
                            serverWorld.registryKey.value.toString()
                        )
                        GymsNbtData.setReturnCoordinates(serverPlayer as EntityDataSaver, pos.toBlockPos())

                        PlayerSpawnHelper.teleportPlayer(
                            serverPlayer,
                            gymDimension,
                            destX,
                            destY,
                            destZ,
                            gym.playerYaw,
                            0.0F
                        )
                    }
                    .build()

                ScheduledTask.Builder()
                    .tracker(ServerTaskTracker)
                    .delay(1f)
                    .execute {
                        val trainerUUIDs = buildTrainers(gym, gymDimension, coords)
                        val chaosTranslatable = translatable(
                            modId("type").withSuffixedPath(".chaos").toTranslationKey()
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
                    }
                    .build()

                debug("Gym $gymType initialized, took ${startTime.elapsedNow().inWholeMilliseconds}ms")
                return true
            } else {
                LOGGER.warn("Gym $gymType could not be initialized, no such type in template registry")
                return false
            }
        }

        return false
    }

    private fun buildTrainers(
        template: GymTemplate,
        gymDimension: ServerWorld,
        coords: BlockPos
    ): List<Pair<UUID, GymTrainer>> {
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

        return trainerIds.map { it.value }
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

    fun handleLeaderBattleWon(serverPlayer: ServerPlayerEntity) {
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return

        val exitPos = BlockPos(
            (gym.coords.x + gym.template.relativeExitBlockSpawn.x).toInt(),
            (gym.coords.y + gym.template.relativeExitBlockSpawn.y).toInt(),
            (gym.coords.z + gym.template.relativeExitBlockSpawn.z).toInt(),
        )

        serverPlayer.world.setBlockState(
            exitPos,
            BlockRegistry.GYM_EXIT.defaultState
        )
        serverPlayer.world.markDirty(exitPos)
        serverPlayer.sendMessage(Text.translatable(modId("message.info.gym_complete").toTranslationKey()))
    }

    fun handleGymLeave(serverPlayer: ServerPlayerEntity) {
        val returnDim = when (GymsNbtData.getReturnDimension(serverPlayer as EntityDataSaver)) {
            null -> DimensionTypes.OVERWORLD_ID
            else -> Identifier.of(GymsNbtData.getReturnDimension(serverPlayer as EntityDataSaver))
        }
        val dim = serverPlayer.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, returnDim))!!
        val returnCoords = GymsNbtData.getReturnCoordinates(serverPlayer as EntityDataSaver) ?: dim.spawnPos

        destructGym(serverPlayer)

        ScheduledTask.Builder()
            .tracker(ServerTaskTracker)
            .execute {
                val returnBlockPos = BlockPos(returnCoords.x, returnCoords.y, returnCoords.z)
                val preloadPos = serverPlayer.world.getChunk(returnBlockPos)
                dim.chunkManager.addTicket(
                    ChunkTicketType.PORTAL,
                    preloadPos.pos,
                    1,
                    returnBlockPos
                )
            }
            .build()

        ScheduledTask.Builder()
            .tracker(ServerTaskTracker)
            .delay(1f)
            .execute {
                returnCoords.let {
                    PlayerSpawnHelper.teleportPlayer(
                        serverPlayer,
                        dim,
                        it.x.toDouble(),
                        it.y.toDouble(),
                        it.z.toDouble(),
                        yaw = serverPlayer.yaw,
                        pitch = serverPlayer.pitch,
                    ).also {
                        debug("Gym instance removed from memory")
                    }
                }
            }
            .build()

        return
    }

    fun destructGym(serverPlayer: ServerPlayerEntity) {
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return

        val world = serverPlayer.world

        gym.npcList.forEach {
            debug("Removing trainer ${it.second} from registry and detaching associated entity")
            RCT.trainerRegistry.unregisterById(it.first.toString())
            (world as ServerWorld).getEntity(it.first)?.discard()
        }

        PLAYER_GYMS.remove(serverPlayer.uuid)
    }

    fun handleLootDistribution(serverPlayer: ServerPlayerEntity) {
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return

        val bundle = ItemStack(Items.BUNDLE)
        val bundleContents = BundleContentsComponent.Builder(BundleContentsComponent.DEFAULT)
        gym.template
            .lootTables
            .filter {
                gym.level in it.levels.first..it.levels.second
            }
            .forEach { table ->
                debug("Settling level ${gym.level} rewards for player ${serverPlayer.name.literalString} after beating leader")
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

        val styledLevel = MutableText.of(Text.of(gym.level.toString()).content).formatted(Formatting.GOLD)
        val styledType = MutableText.of(Text.of(gym.type).content)
            .formatted(Formatting.GREEN)

        bundle.set(
            DataComponentTypes.CUSTOM_NAME,
            translatable(
                modId("gym_reward").toTranslationKey("item"),
                styledLevel, styledType
            )
        )
        bundle.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContents.build())
        bundle.set(DataComponentManager.RAD_GYM_BUNDLE_COMPONENT, true)
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
