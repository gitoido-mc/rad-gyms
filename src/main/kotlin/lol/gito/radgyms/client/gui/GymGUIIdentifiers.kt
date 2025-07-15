/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui

import lol.gito.radgyms.RadGyms.modId

object GymGUIIdentifiers {
    val UI_GYM_ENTER = modId("gym_enter_ui")
    val UI_GYM_ENTRANCE = modId("gym_entrance_ui")
    val UI_GYM_LEAVE = modId("gym_leave_ui")
    val UI_CACHE_ATTUNE = modId("cache_attune_ui")

    // Common IDs
    const val ID_OK = "ok"
    const val ID_START = "start"
    const val ID_CANCEL = "cancel"

    // Gym entrance and leave component IDs
    const val ID_GYM_SLIDER = "gym_slider"
    const val ID_INC = "inc"
    const val ID_INC_TEN = "inc-ten"
    const val ID_DEC = "dec"
    const val ID_DEC_TEN = "dec-ten"
    const val ID_USAGE = "usage"

    // Cache attunement screen IDs
    const val ID_TYPES = "types"
}
