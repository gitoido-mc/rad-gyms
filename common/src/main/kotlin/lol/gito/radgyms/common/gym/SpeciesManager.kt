/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.GymSpecies
import lol.gito.radgyms.common.pokecache.CacheDTO

object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, List<GymSpecies.Container.SpeciesWithForm>> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: Map<String, CacheDTO> = mutableMapOf()

    fun speciesOfType(elementalType: ElementalType): List<GymSpecies.Container.SpeciesWithForm> {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies
            .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies -> associateSpecies.forms }
            .flatMap { (species, forms) ->
                forms
                    .filter { form -> form.types.contains(elementalType) }
                    .map { form ->
                        GymSpecies.Container.SpeciesWithForm(species, form)
                    }
            }
            .toList()
            .sortedBy {
                it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
            }

        if (species.isNotEmpty()) return species

        return allSpecies
            .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies ->
                associateSpecies.forms.filterNot { it.formOnlyShowdownId() in CONFIG.ignoredForms!! }
            }
            .flatMap { (flatMapSpecies, forms) ->
                forms.map {
                    GymSpecies.Container.SpeciesWithForm(flatMapSpecies, it)
                }
            }
            .toList()
            .sortedBy {
                it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
            }
    }

    fun fillPokemonModelFromPokemon(pokemonProperties: PokemonProperties): PokemonModel {
        val poke = pokemonProperties.create()
        poke.setFriendship(100)
        poke.forcedAspects = pokemonProperties.aspects
        poke.updateAspects()
        poke.updateForm()

        return PokemonModel(
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


    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
