/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.net.server.handler

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.net.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.registry.RadGymsItems
import lol.gito.radgyms.common.state.RadGymsState
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

        if (gym != null) {
            if (stack.item == RadGymsItems.EXIT_ROPE) {
                stack.consume(1, player)

                GYM_LEAVE.emit(
                    GymEvents.GymLeaveEvent(
                        reason = GymLeaveReason.USED_ITEM,
                        player = player,
                        gym = gym,
                        type = gym.type,
                        level = gym.level,
                        completed = false,
                        usedRope = true
                    )
                )
            } else {
                GYM_LEAVE.emit(
                    GymEvents.GymLeaveEvent(
                        reason = GymLeaveReason.USED_BLOCK,
                        player = player,
                        gym = gym,
                        type = gym.type,
                        level = gym.level,
                        completed = true,
                        usedRope = false
                    )
                )
            }
        } else {
            GYM_LEAVE.emit(
                GymEvents.GymLeaveEvent(
                    reason = GymLeaveReason.USED_ITEM,
                    player = player,
                    completed = false,
                    usedRope = true
                )
            )
        }
    }
}