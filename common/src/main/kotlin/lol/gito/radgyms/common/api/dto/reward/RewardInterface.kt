package lol.gito.radgyms.common.api.dto.reward

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.api.enumeration.GymReward
import lol.gito.radgyms.common.api.serialization.GymRewardSerializer
import lol.gito.radgyms.common.api.serialization.GymRewardType

interface RewardInterface {
    @SerialName("min_level")
    val minLevel: Int
    @SerialName("max_level")
    val maxLevel: Int
    @Serializable(GymRewardSerializer::class)
    val type: GymReward

    fun getRewardType(): GymRewardType<*>
}
