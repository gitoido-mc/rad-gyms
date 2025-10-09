/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import lol.gito.radgyms.api.events.gui.GymEnterScreenCloseEvent
import lol.gito.radgyms.api.events.gui.GymEnterScreenOpenEvent
import lol.gito.radgyms.api.events.gui.GymLeaveScreenCloseEvent
import lol.gito.radgyms.api.events.gui.GymLeaveScreenOpenEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Suppress("unused")
@Environment(EnvType.CLIENT)
object GuiEvents {
    @JvmField
    val ENTER_SCREEN_OPEN = SimpleObservable<GymEnterScreenOpenEvent>()

    @JvmField
    val ENTER_SCREEN_CLOSE = SimpleObservable<GymEnterScreenCloseEvent>()

    @JvmField
    val LEAVE_SCREEN_OPEN = SimpleObservable<GymLeaveScreenOpenEvent>()

    @JvmField
    val LEAVE_SCREEN_CLOSE = SimpleObservable<GymLeaveScreenCloseEvent>()
}