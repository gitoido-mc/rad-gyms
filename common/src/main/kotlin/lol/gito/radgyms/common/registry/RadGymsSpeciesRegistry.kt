/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.SpeciesWithForm
import lol.gito.radgyms.common.cache.CacheDTO
import lol.gito.radgyms.common.exception.RadGymsSpeciesListEmptyException

private typealias SpeciesWithForms = List<SpeciesWithForm>

private fun Pokemon.matchesSimplified(other: Pokemon): Boolean = this.species == other.species &&
        this.form == other.form &&
        this.aspects.containsAll(other.aspects)

private fun Sequence<Species>.mapToSpeciesWithForms(type: ElementalType? = null): SpeciesWithForms = this
    .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
    .associateWith { associateSpecies ->
        associateSpecies.forms.apply {
            this.add(associateSpecies.standardForm)
        }
    }
    .asSequence()
    .flatMap { (flatMapSpecies, forms) ->
        forms
            .toMutableSet()
            .map {
                SpeciesWithForm(flatMapSpecies, it)
            }
    }
    .toList()
    .filter { speciesPair ->
        if (type != null && !speciesPair.form.types.contains(type)) return@filter false

        val poke = speciesPair.species.create()
        poke.form = speciesPair.form
        poke.forcedAspects = speciesPair.form.aspects.toSet()
        poke.updateAspects()

        if (CONFIG.ignoredSpeciesProps().any { it.matchesSimplified(poke) }) {
            debug("Excluding {} with {} form", poke.species.name, poke.form.name)
            return@filter false
        }

        return@filter true
    }
    .sortedBy {
        it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
    }
    .toList()

object RadGymsSpeciesRegistry {
    var SPECIES_BY_TYPE: HashMap<String, SpeciesWithForms> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: Map<String, CacheDTO> = mutableMapOf()


    fun speciesOfType(elementalType: ElementalType): SpeciesWithForms {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies.mapToSpeciesWithForms(elementalType)

        when (species.isNotEmpty()) {
            true -> return species
            false -> throw RadGymsSpeciesListEmptyException("Cannot get species")
        }
    }

    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
