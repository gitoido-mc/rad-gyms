/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.api.event.ModEvents
import lol.gito.radgyms.common.gym.GymManager.handleGymLeave

class GymLeaveHandler(event: ModEvents.GymLeaveEvent) {
    init {
        debug("gym leave triggered")
        handleGymLeave(event.player)
    }
}