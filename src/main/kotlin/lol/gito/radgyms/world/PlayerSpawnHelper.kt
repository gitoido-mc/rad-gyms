package lol.gito.radgyms.world

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import kotlin.random.Random

object PlayerSpawnHelper {
    fun getUniquePlayerCoords(player: ServerPlayerEntity, world: ServerWorld): BlockPos {
        val border = world.worldBorder
        val seed = Random(player.uuid.mostSignificantBits and border.maxRadius.toLong())
        val playerX: Int = seed.nextInt(
            border.boundNorth.toInt(),
            border.boundSouth.toInt(),
        ) // get uniq x coord based on player uuid
        val playerZ: Int = GymsNbtData.incrementVisitCount(player as EntityDataSaver) * 128

        RadGyms.LOGGER.info("Derived player ${player.name} unique X coordinate from UUID: $playerX")

        return BlockPos(
            playerX,
            0,
            border.boundWest.toInt() + playerZ, // world border
        )
    }

    fun teleportPlayer(
        serverPlayer: ServerPlayerEntity,
        targetDimension: ServerWorld,
        destX: Double,
        destY: Double,
        destZ: Double,
        yaw: Float,
        pitch: Float,
    ) {
        val xpLevels: Int = serverPlayer.experienceLevel
        val xpProgress: Float = serverPlayer.experienceProgress
        val totalExperience: Int = serverPlayer.totalExperience

        serverPlayer.teleport(
            targetDimension,
            destX,
            destY,
            destZ,
            PositionFlag.getFlags(1),
            yaw,
            pitch
        )

        serverPlayer.setExperienceLevel(xpLevels)
        serverPlayer.experienceProgress = xpProgress
        serverPlayer.totalExperience = totalExperience
    }
}