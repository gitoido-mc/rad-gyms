package lol.gito.radgyms.gym

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.resource.Gym
import lol.gito.radgyms.world.PlayerSpawnHelper
import lol.gito.radgyms.world.StructureManager
import lol.gito.radgyms.world.dimension.DimensionManager
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
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

        val gymDimension = player.getServer()?.getWorld(DimensionManager.RADGYMS_LEVEL_KEY)

        if (world.registryKey != DimensionManager.RADGYMS_LEVEL_KEY) {
            val playerDim = world.registryKey.value.toString()
            val pos = player.pos
            val gymType = if (type in GYM_TEMPLATES.keys) type else GYM_TEMPLATES.keys.random()
            val gym = GYM_TEMPLATES[gymType]?.let { GymTemplate.fromGymDto(it, gymLevel, type) }

            if (gym?.structure != null) {
                StructureManager.placeStructure(
                    world,
                    PlayerSpawnHelper.getUniquePlayerCoords(player, world),
                    gym.structure!!
                )
            }

        }

        return false;
    }

    fun register() {
        RadGyms.LOGGER.info("GymManager init")
    }
}