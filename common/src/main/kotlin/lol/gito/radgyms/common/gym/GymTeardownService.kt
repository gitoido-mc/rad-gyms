/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.gym.Gym
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType

object GymTeardownService {
    private var teleportScheduler: GymTeleportScheduler? = null

    fun withTeleportScheduler(teleportScheduler: GymTeleportScheduler): GymTeardownService {
        this.teleportScheduler = teleportScheduler
        return this
    }

    fun destructGym(serverPlayer: ServerPlayer, removeCoords: Boolean = true) {
        if (!RadGymsState.hasGymForPlayer(serverPlayer)) return
        if (removeCoords) RadGymsState.setReturnCoordsForPlayer(serverPlayer, null)

        val gym = RadGymsState.getGymForPlayer(serverPlayer)!!
        val world = serverPlayer.server.getLevel(RadGymsDimensions.GYM_DIMENSION)!!

        gym.npcList.forEach {
            RadGyms.RCT.trainerRegistry.unregisterById(it.toString())
            world.getEntity(it)?.discard()
        }

        RadGymsState.removeGymForPlayer(serverPlayer)
    }

    fun handleGymLeave(serverPlayer: ServerPlayer) {
        try {
            assert(teleportScheduler != null)
        } catch (_: AssertionError) {
            debug("Teleport scheduler not set")
            return
        }

        val state = RadGymsState.getPlayerState(serverPlayer)
        var preloadPos: BlockPos
        var preloadDim: ServerLevel
        if (state.returnCoords != null) {
            debug("Trying to teleport player here: ${state.returnCoords!!.position}")
            preloadPos = state.returnCoords!!.position
            preloadDim = serverPlayer.server.getLevel(
                ResourceKey.create(
                    Registries.DIMENSION,
                    state.returnCoords!!.dimension
                )
            )!!
        } else {
            preloadDim = serverPlayer.server.getLevel(serverPlayer.respawnDimension)!!
            preloadPos = serverPlayer.respawnPosition ?: preloadDim.sharedSpawnPos
        }

        destructGym(serverPlayer)

        preloadDim.chunkSource.addRegionTicket(
            TicketType.PORTAL,
            preloadDim.getChunk(preloadPos).pos,
            2,
            preloadPos
        )

        teleportScheduler!!.scheduleReturnWithCountdown(serverPlayer, preloadDim, preloadPos)
    }

    fun spawnExitBlock(server: MinecraftServer, gym: Gym) {
        val pos = BlockPos(
            (gym.coords.x + gym.template.relativeExitBlockSpawn.x).toInt(),
            (gym.coords.y + gym.template.relativeExitBlockSpawn.y).toInt(),
            (gym.coords.z + gym.template.relativeExitBlockSpawn.z).toInt(),
        )

        debug("Derived exit block: $pos")
        server.getLevel(RadGymsDimensions.GYM_DIMENSION)?.setBlockAndUpdate(
            pos,
            RadGymsBlocks.GYM_EXIT.defaultBlockState()
        )
    }
}
