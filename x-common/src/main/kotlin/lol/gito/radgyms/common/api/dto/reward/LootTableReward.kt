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

data class LootTableReward(
    val id: String,
    @SerializedName("max_items")
    val maxItems: Int? = null,
    @SerializedName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerializedName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    @Transient
    val type: GymReward = GymReward.LOOT_TABLE,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.LOOT_TABLE

    companion object {
        const val ID = "loot_table"

        @JvmStatic
        val CODEC: MapCodec<LootTableReward> =
            RecordCodecBuilder.mapCodec {
                it
                    .group(
                        Codec.STRING.fieldOf("id").forGetter(LootTableReward::id),
                        Codec.INT.lenientOptionalFieldOf("max_items", null).forGetter(LootTableReward::maxItems),
                        Codec.INT.fieldOf("min_level").forGetter(LootTableReward::minLevel),
                        Codec.INT.fieldOf("max_level").forGetter(LootTableReward::maxLevel),
                        GymReward.CODEC.fieldOf("type").forGetter(LootTableReward::type),
                    ).apply(it, ::LootTableReward)
            }
    }
}
