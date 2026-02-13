/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.SpeciesWithForm
import lol.gito.radgyms.common.api.dto.Trainer
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.api.team.TeamGeneratorInterface
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

abstract class GenericTeamGenerator : TeamGeneratorInterface {
    companion object {
        const val GENERATOR_SHINY_ODDS = 10
    }

    protected fun assembleProperties(level: Int, params: String): PokemonProperties = when (params.contains("level=")) {
        true -> PokemonProperties.parse(params)
        false -> PokemonProperties.parse("level=$level $params")
    }

    override fun generateTeam(
        trainer: Trainer,
        level: Int,
        player: ServerPlayer?,
        possibleFormats: MutableList<GymBattleFormat>?,
        types: List<ElementalType>?
    ): MutableList<PokemonModel> {
        val pokemonCount = trainer
            .countPerLevelThreshold
            .filter { it.untilLevel >= level }
            .minByOrNull { it.untilLevel }
            ?.amount ?: 1

        val rawTeam = mutableListOf<PokemonProperties>()

        for (_ in 1..pokemonCount) {
            rawTeam.add(
                generatePokemon(
                    level,
                    trainer.countPerLevelThreshold.count(),
                    types?.random() ?: ElementalTypes.getRandomType()
                ).createPokemonProperties(PokemonPropertyExtractor.ALL)
            )
        }

        val event = GymEvents.GenerateTeamEvent(
            player,
            types ?: ElementalTypes.all().shuffled().take(1),
            level,
            trainer.id,
            trainer.leader,
            rawTeam,
            possibleFormats = mutableListOf(GymBattleFormat.SINGLES)
        )

        val team = mutableListOf<PokemonModel>()

        GENERATE_TEAM.post(event) { generated ->
            generated.team.forEach { props ->
                team.add(createPokemonModel(props))
            }
        }
        return team
    }

    override fun generatePokemon(
        level: Int,
        thresholdAmount: Int,
        type: ElementalType
    ): Pokemon {
        val derived = SPECIES_BY_TYPE[type.showdownId]!!.random()

        return getPokemon(derived, level)
    }

    protected fun getPokemon(
        speciesWithForm: SpeciesWithForm,
        level: Int
    ): Pokemon {
        val poke = speciesWithForm.species.create(level)
        poke.form = speciesWithForm.form
        poke.forcedAspects = speciesWithForm.form.aspects.toSet()
        poke.shiny = (Random.nextInt(1, GENERATOR_SHINY_ODDS) == 1)
        poke.updateAspects()

        return poke
    }

    protected fun createPokemonModel(properties: PokemonProperties) = createPokemonModel(properties.create())

    protected fun createPokemonModel(poke: Pokemon): PokemonModel = PokemonModel(
        poke.species.resourceIdentifier.path,
        poke.gender.toString(),
        poke.level,
        poke.nature.name.path,
        poke.ability.name,
        poke.moveSet.map { it.name }.toSet(),
        PokemonModel.StatsModel(
            poke.ivs.getOrDefault(Stats.HP),
            poke.ivs.getOrDefault(Stats.ATTACK),
            poke.ivs.getOrDefault(Stats.DEFENCE),
            poke.ivs.getOrDefault(Stats.SPECIAL_ATTACK),
            poke.ivs.getOrDefault(Stats.SPECIAL_DEFENCE),
            poke.ivs.getOrDefault(Stats.SPEED),
        ),
        PokemonModel.StatsModel(
            poke.evs.getOrDefault(Stats.HP),
            poke.evs.getOrDefault(Stats.ATTACK),
            poke.evs.getOrDefault(Stats.DEFENCE),
            poke.evs.getOrDefault(Stats.SPECIAL_ATTACK),
            poke.evs.getOrDefault(Stats.SPECIAL_DEFENCE),
            poke.evs.getOrDefault(Stats.SPEED),
        ),
        poke.shiny,
        poke.heldItem().itemHolder.registeredName,
        poke.aspects
    )
}
