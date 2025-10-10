/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.gui

import net.minecraft.util.math.BlockPos

data class GymEnterScreenOpenEvent(
    val pos: BlockPos?,
    val key: Boolean,
    var type: String,
    var minLevel: Int,
    var maxLevel: Int,
    var selectedLevel: Int
)
