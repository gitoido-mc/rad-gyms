/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.battles.ai.StrongBattleAI
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.StrongBattleAIConfig
import com.gitlab.srcmc.rctapi.api.ai.experimental.SelfdotGen5AI
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.common.api.dto.trainer.Trainer
import lol.gito.radgyms.common.api.dto.trainer.TrainerEntityData
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.exception.RadGymsUnknownBattleAIException
import lol.gito.radgyms.common.gym.team.FixedTeamGenerator
import lol.gito.radgyms.common.gym.team.PoolTeamGenerator
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import com.gitlab.srcmc.rctapi.api.models.TrainerModel as RCTTrainerModel
import lol.gito.radgyms.common.api.dto.trainer.TrainerModel as RGTrainerModel

class TrainerFactory(
    private val battleConfigBuilder: BattleConfigFactory = BattleConfigFactory()
) {
    fun create(
        trainer: Trainer, level: Int, player: ServerPlayer?
    ): RGTrainerModel {
        val ai = when (trainer.ai.type) {
            "rct" -> RCTBattleAI(battleConfigBuilder.createFromDto(trainer.ai))
            "cbl" -> StrongBattleAI(trainer.ai.data?.skillLevel ?: StrongBattleAIConfig().skill())
            "sd5" -> SelfdotGen5AI()
            else -> throw RadGymsUnknownBattleAIException(
                "Unknown battle AI type for trainer {}, passed {}, supports only 'rct', 'cbl', 'sd5'"
                    .format(trainer.id, trainer.ai.type)
            )
        }

        val possibleFormats = trainer.possibleFormats.toMutableList()
        val team = when (trainer.teamType) {
            GymTeamType.FIXED -> FixedTeamGenerator.generateTeam(player, trainer, level)
            GymTeamType.POOL -> PoolTeamGenerator.generateTeam(player, trainer, level)
            GymTeamType.GENERATED -> trainer.teamGenerator.instance.generateTeam(
                trainer,
                level,
                player,
                possibleFormats,
                trainer.possibleElementalTypes,
            )
        }

        return RGTrainerModel(
            trainer.id,
            TrainerEntityData(
                name = translatable(trainer.name),
                relativePosition = trainer.spawnRelative.pos.toVec3D(),
                yaw = trainer.spawnRelative.yaw.toFloat()
            ),
            RCTTrainerModel(
                translatable(trainer.name).string,
                JTO.of { ai },
                trainer.bag.map { BagItemModel(it.item, it.quantity) },
                team
            ),
            BattleRules(),
            possibleFormats.random(),
            trainer.leader,
            trainer.requires
        )
    }
}
