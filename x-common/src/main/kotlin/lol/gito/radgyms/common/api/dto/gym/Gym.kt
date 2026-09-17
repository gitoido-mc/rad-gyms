/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.gym

import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.core.BlockPos
import java.util.*

data class Gym(
    val template: GymTemplate,
    val npcList: List<UUID>,
    val coords: BlockPos,
    val level: Int,
    val type: String,
)
