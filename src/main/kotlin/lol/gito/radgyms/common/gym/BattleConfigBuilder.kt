package lol.gito.radgyms.common.gym


import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import lol.gito.radgyms.common.api.dto.TrainerModel

class BattleConfigBuilder {
    fun buildFromDto(dto: TrainerModel.Json.AI): RCTBattleAIConfig {
        var builder = RCTBattleAIConfig.Builder()
        dto.data?.let { data ->
            if (data.moveBias != null) builder = builder.withMoveBias(data.moveBias)
            if (data.statusMoveBias != null) builder = builder.withStatusMoveBias(data.statusMoveBias)
            if (data.switchBias != null) builder = builder.withSwitchBias(data.switchBias)
            if (data.itemBias != null) builder = builder.withItemBias(data.itemBias)
            if (data.maxSelectMargin != null) builder = builder.withMaxSelectMargin(data.maxSelectMargin)
        }
        return builder.build()
    }
}