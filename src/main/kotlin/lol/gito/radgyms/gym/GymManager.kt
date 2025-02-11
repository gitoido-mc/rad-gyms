package lol.gito.radgyms.gym

import com.cobblemon.mod.common.util.toBlockPos
import com.cobblemon.mod.common.util.toVec3d
import com.cobblemon.mod.common.util.toVec3f
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.entity.Trainer
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import lol.gito.radgyms.world.DimensionManager
import lol.gito.radgyms.world.PlayerSpawnHelper
import lol.gito.radgyms.world.StructureManager
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

data class GymInstance(
    val template: GymTemplate,
    val npcList: List<UUID>,
    val coords: BlockPos,
    val level: Int,
)

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, GymDTO> = mutableMapOf()
    val PLAYER_GYMS: MutableMap<UUID, GymInstance> = mutableMapOf()

    fun initInstance(player: ServerPlayerEntity, world: ServerWorld, level: Int, type: String?): Boolean {
        val startTime = markNow()
        val gymLevel: Int = when {
            level < 5 -> 5
            level > 100 -> 100
            else -> level
        }

        val gymDimension = player.getServer()?.getWorld(DimensionManager.RADGYMS_LEVEL_KEY) ?: return false

        if (world.registryKey != DimensionManager.RADGYMS_LEVEL_KEY) {
            val pos = player.pos
            val gymType = if (type in GYM_TEMPLATES.keys) type else GYM_TEMPLATES.keys.random()
            RadGyms.LOGGER.info("Initializing $gymType template for $type gym")
            RadGyms.LOGGER.info("Available templates ${GYM_TEMPLATES.keys}")


            val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }

            if (gym != null) {
                val coords = PlayerSpawnHelper.getUniquePlayerCoords(player, gymDimension)
                val trainerUUIDs = buildFromTemplate(gym, gymDimension, coords)

                if (trainerUUIDs.isNotEmpty()) {
                    PLAYER_GYMS[player.uuid] = GymInstance(gym, trainerUUIDs, coords, gymLevel)
                }
                RadGyms.LOGGER.info("return dim ${world.registryKey.value}")
                GymsNbtData.setReturnDimension(player as EntityDataSaver, world.registryKey.value.toString())
                GymsNbtData.setReturnCoordinates(player as EntityDataSaver, pos.toBlockPos())

                PlayerSpawnHelper.teleportPlayer(
                    player,
                    gymDimension,
                    coords.x + gym.relativePlayerSpawn.x,
                    coords.y + gym.relativePlayerSpawn.y,
                    coords.z + gym.relativePlayerSpawn.z,
                    gym.playerYaw!!,
                    0.0F
                )
                RadGyms.LOGGER.info("Gym $gymType initialized, took ${startTime.elapsedNow().inWholeMilliseconds}ms")
                return true
            } else {
                RadGyms.LOGGER.warn("Gym $gymType could not be initialized, no such type in template registry")
                return false
            }
        }

        return false
    }

    private fun buildFromTemplate(template: GymTemplate, gymDimension: ServerWorld, coords: BlockPos): List<UUID> {
        if (template.structure != null) {
            RadGyms.LOGGER.info("Trying to place gym structure with ${template.structure} at ${coords.x} ${coords.y} ${coords.z} ")
            StructureManager.placeStructure(
                gymDimension,
                coords,
                template.structure!!
            )

            val trainerIds = mutableMapOf<String, UUID>()
            for (gymTrainer in template.trainers) {
                val uuid = UUID.randomUUID()
                trainerIds[gymTrainer.id] = uuid

                val requiredUUID = if (gymTrainer.requires != null) {
                    trainerIds[gymTrainer.requires]!!
                } else {
                    null
                }

                buildTrainerEntity(gymTrainer, gymDimension, coords, uuid, requiredUUID)
            }

            return trainerIds.map { it.value }.toList()
        }
        return emptyList()
    }

    private fun buildTrainerEntity(
        trainer: GymTrainer,
        gymDimension: ServerWorld,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ) {
        val trainerEntity = Trainer(EntityManager.GYM_TRAINER, gymDimension)
        trainerEntity.uuid = trainerUUID
        trainerEntity.headYaw = trainer.npc.yaw
        trainerEntity.bodyYaw = trainer.npc.yaw
        trainerEntity.customName = trainer.npc.name
        trainerEntity.isCustomNameVisible = true
        trainerEntity.setPosition(
            Vec3d(
                coords.x + trainer.npc.relativePosition.x,
                coords.y + trainer.npc.relativePosition.y,
                coords.z + trainer.npc.relativePosition.z
            )
        )
        trainerEntity.trainerId = trainerUUID
        trainerEntity.requires = requiredUUID
        trainerEntity.leader = trainer.leader

        gymDimension.spawnEntityAndPassengers(trainerEntity)
        RadGyms.LOGGER.info("Spawned trainer ${trainerEntity.id} at ${trainerEntity.pos.x} ${trainerEntity.pos.y} ${trainerEntity.pos.z} in ${gymDimension.registryKey.value}")
        val rctTrainer = RadGyms.RCT.trainerRegistry.registerNPC(trainerUUID.toString(), trainer.trainer)
        rctTrainer.entity = trainerEntity
        RadGyms.LOGGER.info("Registered trainer ${trainerEntity.id} in RCT registry with id $trainerUUID")
    }

    fun handleLeaderBattleWon(player: ServerPlayerEntity, world: World) {
        val gym = PLAYER_GYMS[player.uuid] ?: return
        val template = gym.template

        world.setBlockState(
            BlockPos(
                (gym.coords.x + template.relativeExitBlockSpawn.x).toInt(),
                (gym.coords.y + template.relativeExitBlockSpawn.y).toInt(),
                (gym.coords.z + template.relativeExitBlockSpawn.z).toInt(),
            ),
            BlockRegistry.GYM_EXIT.defaultState
        )
    }

    fun handleGymLeave(player: ServerPlayerEntity, world: World) {
        val gym = PLAYER_GYMS[player.uuid] ?: return
        val trainerUUIDList = gym.npcList
        val returnCoords = GymsNbtData.getReturnCoordinates(player as EntityDataSaver)
        val dim = player.getServer()
            ?.getWorld(
                RegistryKey.of(
                    RegistryKeys.WORLD,
                    Identifier.of(GymsNbtData.getReturnDimension(player as EntityDataSaver))
                )
            )
            ?: return

        returnCoords?.let {
            PlayerSpawnHelper.teleportPlayer(
                player,
                dim,
                returnCoords.x.toDouble(),
                returnCoords.y.toDouble(),
                returnCoords.z.toDouble(),
                yaw = player.yaw,
                pitch = player.pitch,
            )
        }

        for (uuid in trainerUUIDList) {
            RadGyms.LOGGER.info("Removing trainer $uuid from registry")
            RadGyms.RCT.trainerRegistry.unregisterById(uuid.toString())
        }

        PLAYER_GYMS.remove(player.uuid)

        val trainersAround = world.getEntitiesByType(
            TypeFilter.instanceOf(Trainer::class.java),
            Box.of(
                // dirty hack to remove persistent trainers after gym is beaten
                player.pos.toVec3f().toVec3d(),
                512.0,
                512.0,
                512.0,
            )
        ) { _ -> true }

        for (trainer in trainersAround) {
            if (trainer.uuid in trainerUUIDList) {
                trainer.detach()
            }
        }
    }

    fun handleLootDistribution(player: ServerPlayerEntity) {
        val gym = PLAYER_GYMS[player.uuid] ?: return

        val lootTables = gym.template.lootTables.filter {
            gym.level in it.levels.first..it.levels.second
        }

        for (table in lootTables) {

            RadGyms.LOGGER.info("Settling level ${gym.level} rewards for player ${player.name.literalString} after beating leader")
            val registryLootTable =
                player.server.reloadableRegistries.registryManager.get(RegistryKeys.LOOT_TABLE).get(table.id)

            val lootContextParameterSet = LootContextParameterSet.Builder(player.world as ServerWorld)
                .add(LootContextParameters.THIS_ENTITY, player)
                .add(LootContextParameters.ORIGIN, player.pos)
                .build(LootContextTypes.GIFT)

            val loot = registryLootTable?.generateLoot(lootContextParameterSet) ?: continue


            for (itemStack in loot) {
                if (!player.giveItemStack(itemStack)) {
                    val pos = player.blockPos
                    val itemEntity =
                        ItemEntity(player.world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), itemStack)
                    player.world.spawnEntity(itemEntity)
                }
            }
        }
    }

    fun register() {
        RadGyms.LOGGER.info("GymManager init")
    }
}