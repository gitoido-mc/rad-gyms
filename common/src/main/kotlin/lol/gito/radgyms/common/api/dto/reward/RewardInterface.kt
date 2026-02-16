package lol.gito.radgyms.common.api.dto.reward

import kotlinx.serialization.SerialName
import lol.gito.radgyms.common.api.serialization.MGymRewardType

interface RewardInterface {
    @SerialName("min_level")
    val minLevel: Int
    @SerialName("max_level")
    val maxLevel: Int

    fun getRewardType(): MGymRewardType<*>
}
