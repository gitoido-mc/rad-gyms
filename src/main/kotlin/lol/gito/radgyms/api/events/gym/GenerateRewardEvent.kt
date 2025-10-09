/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.gym

import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

data class GenerateRewardEvent(
    val player: ServerPlayerEntity,
    val template: GymTemplate,
    val level: Int,
    val type: String,
    val rewards: MutableList<ItemStack> = mutableListOf()
)
