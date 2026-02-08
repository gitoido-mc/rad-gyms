/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym


import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import lol.gito.radgyms.common.api.dto.BattleAI

class BattleConfigFactory {
    fun createFromDto(dto: BattleAI): RCTBattleAIConfig {
        var builder = RCTBattleAIConfig.Builder()

        dto.data?.let { data ->
            if (data.moveBias != null) builder = builder.withMoveBias(data.moveBias)
            if (data.statusMoveBias != null) builder = builder.withStatusMoveBias(data.statusMoveBias)
            if (data.switchBias != null) builder = builder.withSwitchBias(data.switchBias)
            if (data.itemBias != null) builder = builder.withItemBias(data.itemBias)
            if (data.maxSelectMargin != null) builder = builder.withMaxSelectMargin(data.maxSelectMargin)
        }
        return builder.build()
    }
}