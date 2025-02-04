package lol.gito.radgyms.gym

import com.cobblemon.mod.common.util.toBlockPos
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.entity.Trainer
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import lol.gito.radgyms.resource.Gym
import lol.gito.radgyms.world.PlayerSpawnHelper
import lol.gito.radgyms.world.StructureManager
import lol.gito.radgyms.world.dimension.DimensionManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.time.TimeSource.Monotonic.markNow

object GymManager {
    val GYM_TEMPLATES: MutableMap<String, Gym> = mutableMapOf()

    fun initInstance(player: ServerPlayerEntity, world: ServerWorld, level: Int, type: String?): Boolean {
        val startTime = markNow()
        val gymLevel: Int = when {
            level < 5 -> 5
            level > 100 -> 100
            else -> level
        }

        val gymDimension = player.getServer()?.getWorld(DimensionManager.RADGYMS_LEVEL_KEY) ?: return false

        if (world.registryKey != DimensionManager.RADGYMS_LEVEL_KEY) {
            val playerDim = world.registryKey.value.toString()
            val pos = player.pos
            val gymType = if (type in GYM_TEMPLATES.keys) type else GYM_TEMPLATES.keys.random()
            RadGyms.LOGGER.info("Gym type: $gymType")
            RadGyms.LOGGER.info("Gym templates count: ${GYM_TEMPLATES.size}")

            val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }

            if (gym != null) {
                val coords = PlayerSpawnHelper.getUniquePlayerCoords(player, gymDimension)
                buildFromTemplate(gym, gymDimension, coords)

                PlayerSpawnHelper.teleportPlayer(
                    player,
                    gymDimension,
                    coords.x + gym.relativePlayerSpawn.x,
                    coords.y + gym.relativePlayerSpawn.y,
                    coords.z + gym.relativePlayerSpawn.z,
                    gym.playerYaw!!,
                    0.0F
                )
                GymsNbtData.setReturnDimension(player as EntityDataSaver, playerDim)
                GymsNbtData.setReturnCoordinates(player as EntityDataSaver, pos.toBlockPos())
                RadGyms.LOGGER.info("Gym created, took ${startTime.elapsedNow().inWholeMilliseconds}ms")
            } else {
                RadGyms.LOGGER.warn("Gym $gymType could not be loaded")
                return false
            }
        }

        return false;
    }

    private fun buildFromTemplate(template: GymTemplate, gymDimension: ServerWorld, coords: BlockPos) {
        if (template.structure != null) {
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
        }
    }

    private fun buildTrainerEntity(
        trainer: GymTrainer,
        gymDimension: ServerWorld,
        coords: BlockPos,
        trainerUUID: UUID,
        requiredUUID: UUID?
    ) {
        val trainerEntity = Trainer(EntityManager.GYM_TRAINER, gymDimension)
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
        trainerEntity.required = requiredUUID

        gymDimension.spawnEntityAndPassengers(trainerEntity)
        val rctTrainer = RadGyms.RCT.trainerRegistry.registerNPC(trainerUUID.toString(), trainer.trainer)
        rctTrainer.entity = trainerEntity
    }

    fun register() {
        RadGyms.LOGGER.info("GymManager init")
    }
}