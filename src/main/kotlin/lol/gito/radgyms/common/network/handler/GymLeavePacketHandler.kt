/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.network.payload.GymLeave
import lol.gito.radgyms.common.registry.ItemRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text

@Suppress("unused")
class GymLeavePacketHandler(payload: GymLeave, context: ServerPlayNetworking.Context) {
    init {
        val stack = context.player().mainHandStack
        if (stack.item == ItemRegistry.EXIT_ROPE && !context.player().isCreative) {
            stack.decrement(1)
        }
        GymManager.handleGymLeave(context.player())
        context.player().sendMessage(Text.translatable(RadGyms.modId("message.info.gym_failed").toTranslationKey()))
    }
}