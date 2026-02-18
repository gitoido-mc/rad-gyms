/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.reward

import kotlinx.serialization.SerialName
import lol.gito.radgyms.common.api.serialization.MGymRewardType

interface RewardInterface {
    @SerialName("min_level")
    val minLevel: Int

    @SerialName("max_level")
    val maxLevel: Int

    fun getRewardType(): MGymRewardType<*>
}
