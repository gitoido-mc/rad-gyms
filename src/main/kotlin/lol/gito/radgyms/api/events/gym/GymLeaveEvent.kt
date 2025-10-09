/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.gym

import lol.gito.radgyms.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.gym.GymInstance
import net.minecraft.server.network.ServerPlayerEntity

data class GymLeaveEvent(
    val reason: GymLeaveReason,
    val player: ServerPlayerEntity,
    val gym: GymInstance?,
    val type: String?,
    val level: Int?,
    val completed: Boolean?,
    val usedRope: Boolean?
)
