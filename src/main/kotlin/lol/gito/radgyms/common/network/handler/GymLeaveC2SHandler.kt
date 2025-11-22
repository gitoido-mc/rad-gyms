/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.api.enumeration.GymLeaveReason
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.network.payload.GymLeaveC2S
import lol.gito.radgyms.common.registry.ItemRegistry
import lol.gito.radgyms.common.state.RadGymsState
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

@Suppress("unused")
class GymLeaveC2SHandler(payload: GymLeaveC2S, context: ServerPlayNetworking.Context) {
    init {
        val stack = context.player().mainHandStack
        val gym = RadGymsState.getGymForPlayer(context.player())

        if (gym != null) {
            if (stack.item == ItemRegistry.EXIT_ROPE && !context.player().isCreative) {
                stack.decrement(1)

                GYM_LEAVE.emit(
                    GymEvents.GymLeaveEvent(
                        reason = GymLeaveReason.USED_ITEM,
                        player = context.player(),
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
                        player = context.player(),
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
                    player = context.player(),
                    completed = false,
                    usedRope = true
                )
            )
        }
    }
}