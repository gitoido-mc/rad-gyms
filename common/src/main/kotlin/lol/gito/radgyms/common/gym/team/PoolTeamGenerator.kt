/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.trainer.Trainer
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.config.RadGymsConfigs
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

object PoolTeamGenerator : GenericTeamGenerator() {
    fun generateTeam(player: ServerPlayer?, trainer: Trainer, level: Int): MutableList<PokemonModel> {
        val initialAmount = trainer
            .possibleFormats
            .maxOfOrNull {
                it.format.cobblemonBattleFormat.battleType.slotsPerActor
            } ?: 1

        val amount = trainer
            .countPerLevelThreshold
            .filter { it.untilLevel >= level }
            .minByOrNull { it.untilLevel }
            ?.amount ?: initialAmount

        val rawTeam = trainer.team!!
            .shuffled()
            .take(amount)
            .map { setLevel(level, it) }
            .apply { this.forEach { it.updateAspects() } }
            .toMutableList()

        @Suppress("DuplicatedCode")
        val team = mutableListOf<PokemonModel>()

        GENERATE_TEAM.post(
            GymEvents.GenerateTeamEvent(
                player,
                listOf(),
                level,
                trainer.id,
                trainer.leader,
                rawTeam,
                trainer.possibleFormats.toMutableList(),
            ),
        ) { generated ->
            generated.team.forEach { props ->
                val teamShinyChance = RadGymsConfigs.server.trainerTeamShinyChance.coerceAtLeast(1)
                props.shiny = (teamShinyChance > 0 && (Random.nextFloat() < 1 / teamShinyChance))
                team.add(createPokemonModel(props))
            }
        }

        return team
    }
}
