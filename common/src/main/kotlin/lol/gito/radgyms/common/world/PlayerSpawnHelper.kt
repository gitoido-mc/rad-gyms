/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.world

import com.cobblemon.mod.common.util.squeezeWithinBounds
import lol.gito.radgyms.common.GYM_SPACING_IN_DIMENSION
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.portal.DimensionTransition
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.random.Random

object PlayerSpawnHelper {
    fun getUniquePlayerCoords(serverPlayer: ServerPlayer, serverWorld: ServerLevel): BlockPos {
        val border = serverWorld.worldBorder
        val seed = Random(serverPlayer.uuid.mostSignificantBits and border.absoluteMaxSize.toLong())

        val playerX: Int = seed.nextInt(border.minZ.toInt(), border.maxZ.toInt())
        // get uniq z coord based on player uuid
        val playerZ: Int = RadGymsState
            .getPlayerState(serverPlayer)
            .visits * GYM_SPACING_IN_DIMENSION

        debug("Derived player ${serverPlayer.name} unique X coordinate from UUID: $playerX")
        debug("Derived player ${serverPlayer.name} unique Z coordinate from UUID: ${border.minX.toLong() + playerZ}")

        return BlockPos(
            playerX,
            0,
            (border.minX.toLong() + playerZ).toInt(), // world border
        )
    }

    fun teleportPlayer(
        serverPlayerUuid: UUID,
        serverWorld: ServerLevel,
        pos: BlockPos,
        yaw: Float,
        pitch: Float,
    ) {
        RadGyms.implementation.server()?.let { server ->
            server.playerList.getPlayer(serverPlayerUuid)?.let {
                teleportPlayer(it, serverWorld, pos, yaw, pitch)
            }
        }
    }

    fun teleportPlayer(
        serverPlayer: ServerPlayer,
        serverWorld: ServerLevel,
        pos: BlockPos,
        yaw: Float,
        pitch: Float,
    ) {
        // Fix experience just in case
        val xpLevels: Int = serverPlayer.experienceLevel
        val xpProgress: Float = serverPlayer.experienceProgress
        val totalExperience: Int = serverPlayer.totalExperience

        val offset = Vec3(0.0, 1.0, 0.0)
        val finalPos = when (serverWorld.dimension()) {
            RadGymsDimensions.GYM_DIMENSION -> pos.center.add(offset)
            else -> serverWorld.squeezeWithinBounds(pos).center.add(offset)
        }
        val teleportTarget = DimensionTransition(
            serverWorld,
            finalPos,
            Vec3.ZERO,
            yaw,
            pitch,
            DimensionTransition.PLACE_PORTAL_TICKET
        )

        val teleportedPlayer = serverPlayer.changeDimension(teleportTarget) as ServerPlayer
        // Fix experience just in case
        teleportedPlayer.setExperienceLevels(xpLevels)
        teleportedPlayer.experienceProgress = xpProgress
        teleportedPlayer.totalExperience = totalExperience
        debug("Teleported player ${serverPlayer.name}")
    }
}
