package lol.gito.radgyms.common.api.dto.reward

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.MAX_POKE_LEVEL
import lol.gito.radgyms.common.MIN_POKE_LEVEL
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_ADVANCEMENT
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes


@Serializable
@SerialName(REGISTRY_REWARD_TYPE_ADVANCEMENT)
data class AdvancementReward(
    val id: String,
    @SerialName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerialName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    override val type: GymReward = GymReward.ADVANCEMENT,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.ADVANCEMENT

    companion object {
        @JvmStatic
        val CODEC: MapCodec<AdvancementReward> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("id").forGetter(AdvancementReward::id),
                Codec.INT.fieldOf("min_level").forGetter(AdvancementReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(AdvancementReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(AdvancementReward::type),
            ).apply(it, ::AdvancementReward)
        }
    }
}
