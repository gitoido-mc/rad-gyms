/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.registry.ItemRegistry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable

object GymLeavePacketHandler {
    operator fun invoke(
        player: ServerPlayerEntity,
    ) {
        val stack = player.mainHandStack
        if (stack.item == ItemRegistry.EXIT_ROPE && !player.isCreative) {
            stack.decrement(1)
        }
        GymManager.handleGymLeave(player)
        player.sendMessage(translatable(modId("message.info.gym_failed").toTranslationKey()))
    }
}