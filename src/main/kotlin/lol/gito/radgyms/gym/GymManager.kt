package lol.gito.radgyms.gym

import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.entity.Trainer
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import lol.gito.radgyms.world.DimensionManager
import lol.gito.radgyms.world.PlayerSpawnHelper
import lol.gito.radgyms.world.StructureManager
import net.minecraft.entity.ItemEntity
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.dimension.DimensionTypes
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

data class GymInstance(
    val template: GymTemplate,
    val npcList: List<Pair<Int, UUID>>,
    val coords: BlockPos,
    val level: Int,
)

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, GymDTO> = mutableMapOf()
    val PLAYER_GYMS: MutableMap<UUID, GymInstance> = mutableMapOf()

    fun initInstance(serverPlayer: ServerPlayerEntity, serverWorld: ServerWorld, level: Int, type: String?): Boolean {
        val startTime = markNow()
        val gymLevel = level.coerceIn(5..100)
        val gymDimension = serverPlayer.getServer()?.getWorld(DimensionManager.RADGYMS_LEVEL_KEY) ?: return false

        if (serverWorld.registryKey != DimensionManager.RADGYMS_LEVEL_KEY) {
            val pos = serverPlayer.pos
            val gymType = if (type in GYM_TEMPLATES.keys) type else "default"
            LOGGER.info("Initializing $gymType template for $type gym")
            LOGGER.info("Available templates ${GYM_TEMPLATES.keys}")

            val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }
            if (gym != null) {
                val coords = PlayerSpawnHelper.getUniquePlayerCoords(serverPlayer, gymDimension)
                val trainerUUIDs = buildFromTemplate(gym, gymDimension, coords)

                if (trainerUUIDs != null) {
                    PLAYER_GYMS[serverPlayer.uuid] = GymInstance(gym, trainerUUIDs, coords, gymLevel)
                }
                LOGGER.info("return dim ${serverWorld.registryKey.value}")
                GymsNbtData.setReturnDimension(
                    serverPlayer as EntityDataSaver,
                    serverWorld.registryKey.value.toString()
                )
                GymsNbtData.setReturnCoordinates(serverPlayer as EntityDataSaver, pos.toBlockPos())

                PlayerSpawnHelper.teleportPlayer(
                    serverPlayer,
                    gymDimension,
                    coords.x + gym.relativePlayerSpawn.x,
                    coords.y + gym.relativePlayerSpawn.y,
                    coords.z + gym.relativePlayerSpawn.z,
                    gym.playerYaw!!,
                    0.0F
                )
                LOGGER.info("Gym $gymType initialized, took ${startTime.elapsedNow().inWholeMilliseconds}ms")
                return true
            } else {
                LOGGER.warn("Gym $gymType could not be initialized, no such type in template registry")
                return false
            }
        }

        return false
    }

    private fun buildFromTemplate(
        template: GymTemplate,
        gymDimension: ServerWorld,
        coords: BlockPos
    ): List<Pair<Int, UUID>>? {
        if (template.structure != null) {
            val trainerIds = mutableMapOf<String, Pair<Int, UUID>>()

            LOGGER.info("Trying to place gym structure with ${template.structure} at ${coords.x} ${coords.y} ${coords.z} ")
            StructureManager.placeStructure(
                gymDimension,
                coords,
                template.structure!!
            )

            template.trainers.forEach {
                val uuid = UUID.randomUUID()
                val requiredUUID = when (it.requires) {
                    null -> null
                    else -> trainerIds[it.requires]?.second
                }

                buildTrainerEntity(it, gymDimension, coords, uuid, requiredUUID).let { entity ->
                    trainerIds[it.id] = Pair(entity.id, uuid)
                }
            }

            return trainerIds.map { it.value }.toList()
        }

        return null
    }

    private fun buildTrainerEntity(
        trainerTemplate: GymTrainer,
        gymDimension: ServerWorld,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ): Trainer {
        val trainerEntity = Trainer(EntityManager.GYM_TRAINER, gymDimension)
            .apply {
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
                LOGGER.info("Spawning trainer ${it.id} at ${it.pos.x} ${it.pos.y} ${it.pos.z} in ${gymDimension.registryKey.value}")
                gymDimension.spawnEntityAndPassengers(it)
            }

        RCT.trainerRegistry.let { registry ->
            LOGGER.info("Registering trainer ${trainerEntity.id} in RCT registry with id $trainerUUID")
            registry.registerNPC(trainerUUID.toString(), trainerTemplate.trainer).entity = trainerEntity
        }

        return trainerEntity
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
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return
        val returnDim = when (GymsNbtData.getReturnDimension(serverPlayer as EntityDataSaver)) {
            null -> DimensionTypes.OVERWORLD_ID
            else -> Identifier.of(GymsNbtData.getReturnDimension(serverPlayer as EntityDataSaver))
        }
        val dim = serverPlayer.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, returnDim))!!
        val returnCoords = GymsNbtData.getReturnCoordinates(serverPlayer as EntityDataSaver) ?: dim.spawnPos

        gym.npcList.forEach {
            LOGGER.info("Removing trainer ${it.second} from registry and detaching associated entity")
            RCT.trainerRegistry.unregisterById(it.second.toString())
            serverPlayer.world.getEntityById(it.first)?.detach()
        }

        returnCoords.let {
            PlayerSpawnHelper.teleportPlayer(
                serverPlayer,
                dim,
                it.x.toDouble(),
                it.y.toDouble(),
                it.z.toDouble(),
                yaw = serverPlayer.yaw,
                pitch = serverPlayer.pitch,
            )
        }

        PLAYER_GYMS.remove(serverPlayer.uuid)
        LOGGER.info("Gym instance removed from memory")
    }

    fun handleLootDistribution(serverPlayer: ServerPlayerEntity) {
        val gym = PLAYER_GYMS[serverPlayer.uuid] ?: return

        gym.template
            .lootTables
            .filter {
                gym.level in it.levels.first..it.levels.second
            }
            .forEach { table ->
                LOGGER.info("Settling level ${gym.level} rewards for player ${serverPlayer.name.literalString} after beating leader")
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
                    if (!serverPlayer.giveItemStack(itemStack)) {
                        ItemEntity(
                            serverPlayer.world,
                            serverPlayer.pos.x,
                            serverPlayer.pos.y,
                            serverPlayer.pos.z,
                            itemStack
                        ).let {
                            serverPlayer.world.spawnEntity(it)
                        }

                    }
                }
            }
    }

    fun register() {
        LOGGER.info("GymManager init")
    }
}