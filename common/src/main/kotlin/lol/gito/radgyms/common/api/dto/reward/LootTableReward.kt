package lol.gito.radgyms.common.api.dto.reward

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes

@Serializable
data class LootTableReward(
    val id: String,
    override val minLevel: Int = 1,
    override val maxLevel: Int = 100,
    override val type: GymReward = GymReward.LOOT_TABLE,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.LOOT_TABLE

    companion object {
        @JvmStatic
        val CODEC: MapCodec<LootTableReward> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("id").forGetter(LootTableReward::id),
                Codec.INT.fieldOf("min_level").forGetter(LootTableReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(LootTableReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(LootTableReward::type),
            ).apply(it, ::LootTableReward)
        }
    }
}
