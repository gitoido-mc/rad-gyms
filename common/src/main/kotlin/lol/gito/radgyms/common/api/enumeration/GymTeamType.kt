/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.StringRepresentable

@Serializable
enum class GymTeamType : StringRepresentable {
    @JvmField
    @SerialName("generated")
    GENERATED,

    @JvmField
    @SerialName("fixed")
    FIXED,

    @JvmField
    @SerialName("pool")
    POOL;

    override fun getSerializedName(): String = this.name.lowercase()

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymTeamType> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
