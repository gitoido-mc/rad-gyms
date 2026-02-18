/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_ADVANCEMENT
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_COMMAND
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_LOOT_TABLE
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_POKEMON
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.util.StringRepresentable

@Serializable
enum class GymReward : StringRepresentable {
    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_LOOT_TABLE)
    LOOT_TABLE,

    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_COMMAND)
    COMMAND,

    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_POKEMON)
    POKEMON,

    @JvmField
    @SerialName(REGISTRY_REWARD_TYPE_ADVANCEMENT)
    ADVANCEMENT;

    override fun getSerializedName(): String = this.name.lowercase()

    override fun toString(): String = modId(this.name.lowercase()).toString()

    companion object {
        @JvmField
        @Transient
        val CODEC: StringRepresentable.StringRepresentableCodec<GymReward> =
            StringRepresentable.fromEnum { entries.toTypedArray() }
    }
}
