/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.GymSpecies
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.api.team.TeamGeneratorInterface
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

abstract class GenericTeamGenerator : TeamGeneratorInterface {
    override fun generateTeam(
        trainer: TrainerModel.Json.Trainer,
        level: Int,
        player: ServerPlayer,
        possibleFormats: MutableList<GymBattleFormat>,
        types: List<ElementalType>
    ): MutableList<PokemonModel> {
        var pokemonCount = 1
        for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
            if (level >= mapperLevel[0]) pokemonCount = mapperLevel[1]
        }

        val rawTeam = mutableListOf<PokemonProperties>()

        (1..pokemonCount).forEach { _ ->
            rawTeam.add(generatePokemon(level, trainer.countPerLevelThreshold.count(), types.random()))
        }

        val event = GymEvents.GenerateTeamEvent(
            player,
            types,
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

    override fun generatePokemon(
        level: Int,
        thresholdAmount: Int,
        type: ElementalType
    ): PokemonProperties {
        val derived = SPECIES_BY_TYPE[type.showdownId]!!.random()

        return getPokemonProperties(derived, level)
    }

    protected fun getPokemonProperties(speciesWithForm: GymSpecies.Container.SpeciesWithForm, level: Int): PokemonProperties {

        var pokeString =
            "${speciesWithForm.species.resourceIdentifier.path} form=${speciesWithForm.form.name} level=${level}"

        if (Random.nextInt(1, 10) == 1) {
            pokeString = pokeString.plus(" shiny=yes")
        }

        val pokemonProperties: PokemonProperties = pokeString.toProperties()

        // Thanks Ludichat [Cobbreeding project code]
        if (pokemonProperties.form != null) {
            speciesWithForm.species.standardForm
            speciesWithForm.species.forms.find { it.formOnlyShowdownId() == pokemonProperties.form }?.run {
                aspects.forEach {
                    // alternative form
                    pokemonProperties.customProperties.add(FlagSpeciesFeature(it, true))
                    // regional bias
                    pokemonProperties.customProperties.add(
                        StringSpeciesFeature(
                            "region_bias",
                            it.split("-").last()
                        )
                    )
                    // Basculin wants to be special
                    // We're handling aspects now but some form handling should be kept to prevent
                    // legitimate abilities to be flagged as forced
                    pokemonProperties.customProperties.add(
                        StringSpeciesFeature(
                            "fish_stripes",
                            it.removeSuffix("striped")
                        )
                    )
                }
            }
        }

        return pokemonProperties
    }
}