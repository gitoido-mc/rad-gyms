/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.util.StringRepresentable

enum class GymReward : StringRepresentable {
    @JvmField
    LOOT_TABLE,

    @JvmField
    COMMAND,

    @JvmField
    POKEMON,

    @JvmField
    ADVANCEMENT,
    ;

    override fun getSerializedName(): String = this.name.lowercase()

    override fun toString(): String = modId(this.name.lowercase()).toString()

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymReward> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
