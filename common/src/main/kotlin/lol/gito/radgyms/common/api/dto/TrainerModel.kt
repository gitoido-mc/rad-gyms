/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto

import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat

data class TrainerModel(
    val id: String,
    val npc: TrainerEntityData,
    val trainer: TrainerModel,
    val battleRules: BattleRules,
    val format: GymBattleFormat = GymBattleFormat.SINGLES,
    val leader: Boolean = false,
    val requires: String? = null
)
