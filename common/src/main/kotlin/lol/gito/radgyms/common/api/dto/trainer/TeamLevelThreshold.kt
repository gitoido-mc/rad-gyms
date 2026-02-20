/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.trainer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamLevelThreshold(
    val amount: Int,
    @SerialName("until_level")
    val untilLevel: Int,
)
