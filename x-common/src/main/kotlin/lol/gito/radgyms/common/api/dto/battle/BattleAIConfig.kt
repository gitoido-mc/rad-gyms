/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.battle

import com.google.gson.annotations.SerializedName

data class BattleAIConfig(
    // RCTBattleAI params
    @SerializedName("move_bias")
    val moveBias: Double? = null,
    @SerializedName("status_move_bias")
    val statusMoveBias: Double? = null,
    @SerializedName("switch_bias")
    val switchBias: Double? = null,
    @SerializedName("item_bias")
    val itemBias: Double? = null,
    @SerializedName("max_select_margin")
    val maxSelectMargin: Double? = null,
    // StrongBattleAI
    @SerializedName("skill_level")
    val skillLevel: Int? = null,
)
