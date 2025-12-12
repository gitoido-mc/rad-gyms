/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.GymSpecies
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import net.minecraft.server.level.ServerPlayer
import kotlin.collections.chunked
import kotlin.collections.count
import kotlin.collections.forEach
import kotlin.collections.random
import kotlin.random.Random
import kotlin.text.removeSuffix
import kotlin.text.split

abstract class AbstractTeamGenerator {
    fun generate(
        trainer: TrainerModel.Json.Trainer,
        level: Int,
        elementTypes: List<ElementalType>,
        player: ServerPlayer,
        possibleFormats: MutableList<GymBattleFormat>
    ): MutableList<PokemonModel> {
        var pokemonCount = 1
        for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
            if (level >= mapperLevel[0]) pokemonCount = mapperLevel[1]
        }

        val rawTeam = mutableListOf<PokemonProperties>()

        (1..pokemonCount).forEach { _ ->
            rawTeam.add(generatePokemon(level, elementTypes.random(), trainer.countPerLevelThreshold.count()))
        }

        val event = GymEvents.GenerateTeamEvent(
            player,
            elementTypes,
            level,
            trainer.id,
            trainer.leader,
            rawTeam,
            possibleFormats
        )

        val team = mutableListOf<PokemonModel>()

        GENERATE_TEAM.post(event) { generated ->
            generated.team.forEach { props ->
                team.add(fillPokemonModelFromPokemon(props))
            }
        }
        return team
    }

    abstract fun generatePokemon(level: Int, type: ElementalType, thresholdAmount: Int): PokemonProperties
}