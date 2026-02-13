/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.Trainer
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import net.minecraft.server.level.ServerPlayer

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
            .map { assembleProperties(level, it) }
            .apply { this.forEach { it.updateAspects() } }
            .toMutableList()

        val possibleTypes = rawTeam
            .map { ElementalTypes.get(it.type!!)!! }
            .toMutableSet()

        val event = GymEvents.GenerateTeamEvent(
            player,
            possibleTypes.toList(),
            level,
            trainer.id,
            trainer.leader,
            rawTeam,
            trainer.possibleFormats.toMutableList()
        )

        val team = mutableListOf<PokemonModel>()

        GENERATE_TEAM.post(event) { generated ->
            generated.team.forEach { props ->
                team.add(createPokemonModel(props))
            }
        }

        return team
    }
}
