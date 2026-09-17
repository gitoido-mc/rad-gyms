/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleFormatProvider
import net.minecraft.util.StringRepresentable
import com.cobblemon.mod.common.battles.BattleFormat as CBattleFormat

enum class GymBattleFormat :
    BattleFormatProvider,
    StringRepresentable {
    @JvmField
    SINGLES {
        override val format: BattleFormat = BattleFormat.GEN_9_SINGLES
    },

    @JvmField
    DOUBLES {
        override val format: BattleFormat = BattleFormat.GEN_9_DOUBLES
    },

    @JvmField
    TRIPLES {
        override val format: BattleFormat = BattleFormat.GEN_9_TRIPLES
    }, ;

    override fun getSerializedName(): String = this.format.cobblemonBattleFormat.battleType.name

    abstract val format: BattleFormat

    override fun getCobblemonBattleFormat(): CBattleFormat = this.format.cobblemonBattleFormat

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymBattleFormat> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
