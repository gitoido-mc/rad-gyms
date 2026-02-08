/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Species
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.SpeciesWithForm
import lol.gito.radgyms.common.cache.CacheDTO

private typealias SpeciesWithForms = List<SpeciesWithForm>

private fun Sequence<Species>.mapToSpeciesWithForms(elementalType: ElementalType? = null): SpeciesWithForms = this
    .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
    .associateWith { associateSpecies ->
        associateSpecies.forms.apply {
            this.add(associateSpecies.standardForm)
        }
    }
    .flatMap { (flatMapSpecies, forms) ->
        forms
            .toMutableSet()
            .map {
                SpeciesWithForm(flatMapSpecies, it)
            }
    }
    .toList()
    .filter {
        when (elementalType) {
            null -> true
            else -> {
                it.form.types.contains(elementalType)
            }
        }
    }
    .sortedBy {
        it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
    }


object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, SpeciesWithForms> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: Map<String, CacheDTO> = mutableMapOf()


    fun speciesOfType(elementalType: ElementalType): SpeciesWithForms {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies.mapToSpeciesWithForms(elementalType)

        when (species.isNotEmpty()) {
            true -> return species
            false -> throw RuntimeException("Cannot get species")
        }
    }

    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
