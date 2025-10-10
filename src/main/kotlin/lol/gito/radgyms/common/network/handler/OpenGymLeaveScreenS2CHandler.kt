/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.api.events.GuiEvents
import lol.gito.radgyms.api.events.gui.GymLeaveScreenOpenEvent
import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class OpenGymLeaveScreenS2CHandler(payload: OpenGymLeaveScreenS2C, context: ClientPlayNetworking.Context) {
    init {
        GuiEvents.LEAVE_SCREEN_OPEN.emit(
            GymLeaveScreenOpenEvent(payload.id)
        )
    }
}