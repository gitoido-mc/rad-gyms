/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.server.handler

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.net.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object GymLeaveC2SHandler : ServerNetworkPacketHandler<GymLeaveC2S> {
    override fun handle(
        packet: GymLeaveC2S,
        server: MinecraftServer,
        player: ServerPlayer
    ) {
        val stack = player.mainHandItem
        val gym = RadGymsState.getGymForPlayer(player)

        if (gym == null) {
            debug("Gym is null")
            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.USED_ITEM,
                    player = player,
                    completed = false,
                    usedRope = true
                )
            )
            return
        }

        if (stack.item == RadGymsItems.EXIT_ROPE) {
            debug("Player used exit rope")
            stack.consume(1, player)

            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.USED_ITEM,
                    player = player,
                    gym = gym,
                    completed = false,
                    usedRope = true
                )
            )
        } else {
            debug("Player used exit block")
            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.USED_BLOCK,
                    player = player,
                    gym = gym,
                    completed = true,
                    usedRope = false
                )
            )
        }
    }
}
