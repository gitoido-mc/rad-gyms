package lol.gito.radgyms.common.api.dto.reward

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.MAX_POKE_LEVEL
import lol.gito.radgyms.common.MIN_POKE_LEVEL
import lol.gito.radgyms.common.REGISTRY_REWARD_TYPE_LOOT_TABLE
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardType
import lol.gito.radgyms.common.api.serialization.GymRewardTypes

@Serializable
@SerialName(REGISTRY_REWARD_TYPE_LOOT_TABLE)
data class LootTableReward(
    val id: String,
    @SerialName("max_items")
    val maxItems: Int? = null,
    @SerialName("min_level")
    override val minLevel: Int = MIN_POKE_LEVEL,
    @SerialName("max_level")
    override val maxLevel: Int = MAX_POKE_LEVEL,
    override val type: GymReward = GymReward.LOOT_TABLE,
) : RewardInterface {
    override fun getRewardType(): GymRewardType<*> = GymRewardTypes.LOOT_TABLE

    companion object {
        @JvmStatic
        val CODEC: MapCodec<LootTableReward> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("id").forGetter(LootTableReward::id),
                Codec.INT.lenientOptionalFieldOf("max_items", null).forGetter(LootTableReward::maxItems),
                Codec.INT.fieldOf("min_level").forGetter(LootTableReward::minLevel),
                Codec.INT.fieldOf("max_level").forGetter(LootTableReward::maxLevel),
                GymReward.CODEC.fieldOf("type").forGetter(LootTableReward::type),
            ).apply(it, ::LootTableReward)
        }
    }
}
