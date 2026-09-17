/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.reward

import com.google.gson.annotations.SerializedName
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import lol.gito.radgyms.common.MAX_POKE_LEVEL
import lol.gito.radgyms.common.MIN_POKE_LEVEL
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes

data class CommandReward(
    val execute: String,
    @SerializedName("as_server")
    val asServer: Boolean = false,
    @SerializedName("op_level")
    val opLevel: Int = 2,
    @SerializedName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerializedName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    @Transient
    val type: GymReward = GymReward.COMMAND,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.COMMAND

    companion object {
        const val ID = "command"

        @JvmStatic
        val CODEC: MapCodec<CommandReward> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codec.STRING.fieldOf("execute").forGetter(CommandReward::execute),
                        Codec.BOOL.fieldOf("as_server").forGetter(CommandReward::asServer),
                        Codec.INT.fieldOf("op_level").forGetter(CommandReward::opLevel),
                        Codec.INT.fieldOf("min_level").forGetter(CommandReward::minLevel),
                        Codec.INT.fieldOf("max_level").forGetter(CommandReward::maxLevel),
                        GymReward.CODEC.fieldOf("type").forGetter(CommandReward::type),
                    ).apply(instance, ::CommandReward)
            }
    }
}
