/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import lol.gito.radgyms.api.events.GuiEvents
import lol.gito.radgyms.api.events.gui.GymEnterScreenOpenEvent
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.network.payload.OpenGymEnterScreenS2C
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

@Suppress("unused")
class OpenGymEnterScreenS2CHandler(payload: OpenGymEnterScreenS2C, context: ClientPlayNetworking.Context) {
    init {
        GuiEvents.ENTER_SCREEN_OPEN.emit(
            GymEnterScreenOpenEvent(
                payload.pos,
                payload.key,
                payload.type,
                RadGyms.CONFIG.minLevel,
                RadGyms.CONFIG.maxLevel,
            )
        )
    }
}