package lol.gito.radgyms.common.api.dto.reward

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes


@Serializable
data class CommandReward(
    val execute: String,
    override val minLevel: Int = 1,
    override val maxLevel: Int = 100,
    override val type: GymReward = GymReward.COMMAND,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.COMMAND

    companion object {
        @JvmStatic
        val CODEC: MapCodec<CommandReward> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("execute").forGetter(CommandReward::execute),
                Codec.INT.fieldOf("min_level").forGetter(CommandReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(CommandReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(CommandReward::type),
            ).apply(it, ::CommandReward)
        }
    }
}
