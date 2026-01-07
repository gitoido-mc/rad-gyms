/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.gym.GymTeardownService
import lol.gito.radgyms.common.gym.GymTeleportScheduler

object GymLeaveHandler {
    @JvmStatic
    fun execute(event: GymEvents.GymLeaveEvent) {
        debug("gym leave triggered")
        if (event.completed == false) {
            GymTeardownService.destructGym(event.player)
            return
        }

        GymTeardownService
            .withTeleportScheduler(GymTeleportScheduler())
            .handleGymLeave(event.player)
    }
}