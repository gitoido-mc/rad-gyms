/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.gui

import lol.gito.radgyms.api.enumeration.GuiScreenCloseChoice
import net.minecraft.util.math.BlockPos

data class GymEnterScreenCloseEvent(
    val choice: GuiScreenCloseChoice,
    val key: Boolean,
    val level: Int,
    val type: String? = null,
    val pos: BlockPos? = null
)