/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleFormatProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.cobblemon.mod.common.battles.BattleFormat as CBattleFormat

@Serializable
enum class GymBattleFormat: BattleFormatProvider {
    @JvmField
    @SerialName("singles")
    SINGLES {
        override val format: BattleFormat = BattleFormat.GEN_9_SINGLES
    },

    @JvmField
    @SerialName("doubles")
    DOUBLES {
        override val format: BattleFormat = BattleFormat.GEN_9_DOUBLES
    },

    @JvmField
    @SerialName("triples")
    TRIPLES {
        override val format: BattleFormat = BattleFormat.GEN_9_TRIPLES
    };

    abstract val format: BattleFormat

    override fun getCobblemonBattleFormat(): CBattleFormat = this.format.cobblemonBattleFormat
}