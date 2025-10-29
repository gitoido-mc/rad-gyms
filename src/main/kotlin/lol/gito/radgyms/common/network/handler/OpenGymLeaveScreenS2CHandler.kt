/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.api.events.ModEvents
import lol.gito.radgyms.client.registry.GuiEvents.LEAVE_SCREEN_OPEN
import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Suppress("unused")
class OpenGymLeaveScreenS2CHandler(payload: OpenGymLeaveScreenS2C, context: ClientPlayNetworking.Context) {
    init {
        LEAVE_SCREEN_OPEN.emit(
            ModEvents.GymLeaveScreenOpenEvent(payload.id)
        )
    }
}