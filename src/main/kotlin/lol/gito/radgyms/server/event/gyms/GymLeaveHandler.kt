/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.event.gyms

import lol.gito.radgyms.api.events.gym.GymLeaveEvent
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.gym.GymManager.destructGym

class GymLeaveHandler(event: GymLeaveEvent) {
    init {
        debug("gym leave triggered")
        destructGym(event.player, true)
    }
}