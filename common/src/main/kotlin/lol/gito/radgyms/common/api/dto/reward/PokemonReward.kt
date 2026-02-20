/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.reward

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import lol.gito.radgyms.common.MAX_POKE_LEVEL
import lol.gito.radgyms.common.MIN_POKE_LEVEL
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_POKEMON
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.KPokemonPropertiesSerializer
import lol.gito.radgyms.common.api.serialization.MGymRewardType
import lol.gito.radgyms.common.api.serialization.MGymRewardTypes

@Serializable
@SerialName(REGISTRY_REWARD_TYPE_POKEMON)
data class PokemonReward(
    @Serializable(KPokemonPropertiesSerializer::class)
    val pokemon: PokemonProperties,
    @SerialName("min_perfect_ivs")
    val minPerfectIvs: Int? = null,
    @SerialName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerialName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    @Transient
    val type: GymReward = GymReward.POKEMON,
) : RewardInterface {
    override fun getRewardType(): MGymRewardType<*> = MGymRewardTypes.POKEMON

    companion object {
        @JvmStatic
        val CODEC: MapCodec<PokemonReward> = RecordCodecBuilder.mapCodec {
            it.group(
                PokemonProperties.CODEC.fieldOf("pokemon").forGetter(PokemonReward::pokemon),
                Codec.INT.lenientOptionalFieldOf("min_perfect_ivs", null).forGetter(PokemonReward::minPerfectIvs),
                Codec.INT.fieldOf("min_level").forGetter(PokemonReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(PokemonReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(PokemonReward::type),
            ).apply(it, ::PokemonReward)
        }
    }
}
