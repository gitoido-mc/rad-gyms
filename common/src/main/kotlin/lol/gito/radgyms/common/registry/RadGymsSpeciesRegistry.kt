/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Species
import lol.gito.radgyms.common.RadGyms.config
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.SpeciesWithForm
import lol.gito.radgyms.common.cache.CacheDTO
import lol.gito.radgyms.common.exception.RadGymsSpeciesListEmptyException

private typealias SpeciesWithForms = List<SpeciesWithForm>

private fun PokemonProperties.matchesSimplified(other: SpeciesWithForm): Boolean {
    if ((this.species != null && this.species != "random") && this.aspects.isNotEmpty()) {
        return this.species == other.species.resourceIdentifier.path && other.form.aspects.containsAll(this.aspects)
    }

    if (this.species == null && this.aspects.isNotEmpty()) {
        return other.form.aspects.containsAll(this.aspects)
    }

    return this.species == other.species.resourceIdentifier.path
}

private fun mapToSpeciesWithForms(
    list: List<Species>,
    type: ElementalType? = null,
): SpeciesWithForms {
    val ignored = config.ignoredSpecies?.map { PokemonProperties.parse(it) } ?: emptyList()

    val filtered =
        list // Make a copy
            .filterNot { it.resourceIdentifier.path in config.ignoredSpecies!! }
            .associateWith { associateSpecies ->
                val forms = mutableListOf<FormData>()
                forms.addAll(associateSpecies.forms)
                forms.add(associateSpecies.standardForm)
                return@associateWith forms.toMutableSet()
            }.flatMap { (flatMapSpecies, forms) ->
                forms.map {
                    SpeciesWithForm(flatMapSpecies, it)
                }
            }.filter { speciesPair ->
                if (type != null && !speciesPair.form.types.contains(type)) return@filter false

                val poke = speciesPair.species.create()
                poke.form = speciesPair.form
                poke.updateAspects()

                if (ignored.any { it.matchesSimplified(speciesPair) }) {
                    debug(
                        "Excluding {} with aspects: {}",
                        poke.species.name,
                        poke.form.aspects.joinToString(" "),
                    )
                    return@filter false
                }

                return@filter true
            }.sortedBy {
                it.form.baseStats
                    .filterKeys { key -> key.type == Stat.Type.PERMANENT }
                    .values
                    .sum()
            }

    return filtered
}

object RadGymsSpeciesRegistry {
    var speciesByType: HashMap<String, SpeciesWithForms> = HashMap(ElementalTypes.count())
    var speciesByRarity: Map<String, CacheDTO> = mutableMapOf()

    fun speciesOfType(
        species: List<Species>,
        elementalType: ElementalType,
    ): SpeciesWithForms {
        val filtered = mapToSpeciesWithForms(species, elementalType)

        when (filtered.isNotEmpty()) {
            true -> return filtered
            false -> throw RadGymsSpeciesListEmptyException("Cannot get species")
        }
    }

    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
