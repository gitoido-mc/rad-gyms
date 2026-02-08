/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BattleAIConfig(
    // RCTBattleAI params
    @SerialName("move_bias")
    val moveBias: Double? = null,
    @SerialName("status_move_bias")
    val statusMoveBias: Double? = null,
    @SerialName("switch_bias")
    val switchBias: Double? = null,
    @SerialName("item_bias")
    val itemBias: Double? = null,
    @SerialName("max_select_margin")
    val maxSelectMargin: Double? = null,
    // StrongBattleAI
    @SerialName("skill_level")
    val skillLevel: Int? = null
)
