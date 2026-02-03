/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.battles.ai.StrongBattleAI
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.StrongBattleAIConfig
import com.gitlab.srcmc.rctapi.api.ai.experimental.SelfdotGen5AI
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import com.gitlab.srcmc.rctapi.api.models.TrainerModel as RCTTrainerModel
import lol.gito.radgyms.common.api.dto.TrainerModel as RGTrainerModel

class TrainerFactory(
    private val battleConfigBuilder: BattleConfigFactory = BattleConfigFactory()
) {
    fun create(
        trainer: RGTrainerModel.Json.Trainer, level: Int, player: ServerPlayer
    ): RGTrainerModel {
        val ai = when (trainer.ai.type) {
            "rct" -> RCTBattleAI(battleConfigBuilder.createFromDto(trainer.ai))
            "cbl" -> StrongBattleAI(trainer.ai.data?.skillLevel?:run { StrongBattleAIConfig().skill() })
            "sd5" -> SelfdotGen5AI()
            else -> throw RuntimeException(
                "Unknown battle AI type for trainer ${trainer.id}, passed ${trainer.ai.type}, supports only 'rct', 'cbl', 'sd5'"
            )
        }

        val bag = trainer.bag.map { BagItemModel(it.item, it.quantity) }
        val possibleFormats = trainer.possibleFormats.toMutableList()
        val team = when (trainer.teamType) {
            GymTeamType.GENERATED -> trainer.teamGenerator.instance.generateTeam(
                trainer,
                level,
                player,
                possibleFormats,
                trainer.possibleElementalTypes?:run { ElementalTypes.all().shuffled().take(1) },
            )

            GymTeamType.POOL -> {
                var pokemonCount = 1
                // todo: make it better
                for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
                    if (level >= mapperLevel[0]) pokemonCount = mapperLevel[1]
                }
                // now that we know amount of pokes we want to take - we fill the data
                trainer.team
                    .shuffled()
                    .take(pokemonCount)
                    .map { params ->
                        val props = PokemonProperties.parse(params)

                        fillPokemonModelFromPokemon(props)
                    }
                    .toMutableList()
            }

            GymTeamType.FIXED -> trainer.team
                .map { params ->
                    val props = PokemonProperties.parse("level=$level $params")
                    fillPokemonModelFromPokemon(props)
                }
                .toMutableList()
        }

        return RGTrainerModel(
            trainer.id,
            RGTrainerModel.EntityData(
                name = translatable(trainer.name),
                relativePosition = trainer.spawnRelative.pos.toVec3D(),
                yaw = trainer.spawnRelative.yaw.toFloat()
            ),
            RCTTrainerModel(
                translatable(trainer.name).string,
                JTO.of { ai },
                bag,
                team
            ),
            BattleRules(),
            possibleFormats.random(),
            trainer.leader,
            trainer.requires
        )
    }
}