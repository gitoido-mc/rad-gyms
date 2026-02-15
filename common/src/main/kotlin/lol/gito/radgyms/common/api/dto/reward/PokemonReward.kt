package lol.gito.radgyms.common.api.dto.reward

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.MAX_POKE_LEVEL
import lol.gito.radgyms.common.MIN_POKE_LEVEL
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_POKEMON
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes
import lol.gito.radgyms.common.api.serialization.PokemonPropertiesSerializer

@Serializable
@SerialName(REGISTRY_REWARD_TYPE_POKEMON)
data class PokemonReward(
    @Serializable(PokemonPropertiesSerializer::class)
    val pokemon: PokemonProperties,
    @SerialName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerialName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    override val type: GymReward = GymReward.POKEMON
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.POKEMON

    companion object {
        @JvmStatic
        val CODEC: MapCodec<PokemonReward> = RecordCodecBuilder.mapCodec {
            it.group(
                PokemonProperties.CODEC.fieldOf("pokemon").forGetter(PokemonReward::pokemon),
                Codec.INT.fieldOf("min_level").forGetter(PokemonReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(PokemonReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(PokemonReward::type),
            ).apply(it, ::PokemonReward)
        }
    }
}
