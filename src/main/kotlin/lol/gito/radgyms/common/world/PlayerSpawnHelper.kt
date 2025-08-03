/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.world

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.nbt.EntityDataSaver
import lol.gito.radgyms.common.nbt.GymsNbtData
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import kotlin.random.Random

object
PlayerSpawnHelper {
    fun getUniquePlayerCoords(serverPlayer: ServerPlayerEntity, serverWorld: ServerWorld): BlockPos {
        val border = serverWorld.worldBorder
        val seed = Random(serverPlayer.uuid.mostSignificantBits and border.maxRadius.toLong())
        val playerX: Int = seed.nextInt(
            border.boundNorth.toInt(),
            border.boundSouth.toInt(),
        ) // get uniq x coord based on player uuid
        val playerZ: Int = GymsNbtData.incrementVisitCount(serverPlayer as EntityDataSaver) * 128

        debug("Derived player ${serverPlayer.name} unique X coordinate from UUID: $playerX")
        debug("Derived player ${serverPlayer.name} unique Z coordinate from UUID: ${border.boundWest.toLong() + playerZ}")

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

        val teleportTarget = TeleportTarget(
            serverWorld,
            Vec3d(destX, destY, destZ),
            Vec3d.ZERO,
            yaw,
            pitch,
            TeleportTarget.NO_OP
        )

        val teleportedPlayer = serverPlayer.teleportTo(teleportTarget) as ServerPlayerEntity
        // Fix experience just in case
        teleportedPlayer.setExperienceLevel(xpLevels)
        teleportedPlayer.experienceProgress = xpProgress
        teleportedPlayer.totalExperience = totalExperience
        debug("Teleported player ${serverPlayer.name}")
    }
}
