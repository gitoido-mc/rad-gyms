package lol.gito.radgyms.world

import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.nbt.EntityDataSaver
import lol.gito.radgyms.nbt.GymsNbtData
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import kotlin.random.Random

object PlayerSpawnHelper {
    fun getUniquePlayerCoords(serverPlayer: ServerPlayerEntity, serverWorld: ServerWorld): BlockPos {
        val border = serverWorld.worldBorder
        val seed = Random(serverPlayer.uuid.mostSignificantBits and border.maxRadius.toLong())
        val playerX: Int = seed.nextInt(
            border.boundNorth.toInt(),
            border.boundSouth.toInt(),
        ) // get uniq x coord based on player uuid
        val playerZ: Int = GymsNbtData.incrementVisitCount(serverPlayer as EntityDataSaver) * 128

        LOGGER.info("Derived player ${serverPlayer.name} unique X coordinate from UUID: $playerX")

        return BlockPos(
            playerX,
            0,
            border.boundWest.toInt() + playerZ, // world border
        )
    }

    fun teleportPlayer(
        serverPlayer: ServerPlayerEntity,
        serverWorld: ServerWorld,
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
            serverWorld,
            destX,
            destY,
            destZ,
            PositionFlag.getFlags(1),
            yaw,
            pitch
        )
        LOGGER.info("Teleported player ${serverPlayer.name}")

        serverPlayer.setExperienceLevel(xpLevels)
        serverPlayer.experienceProgress = xpProgress
        serverPlayer.totalExperience = totalExperience
    }
}