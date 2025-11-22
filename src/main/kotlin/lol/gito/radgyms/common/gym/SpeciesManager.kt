/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.RadGyms.CONFIG
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.common.pokecache.CacheDTO
import kotlin.random.Random

object SpeciesManager {
    var SPECIES_BY_TYPE: HashMap<String, List<Pair<Species, FormData>>> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: MutableMap<String, CacheDTO> = mutableMapOf()

    fun speciesOfType(elementalType: ElementalType): List<Pair<Species, FormData>> {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies
            .filter { filterSpecies -> filterSpecies.name !in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies -> associateSpecies.forms.filter { form -> form.name !in CONFIG.ignoredForms!! } }
            .flatMap { (flatMapSpecies, forms) ->
                forms.filter { form -> form.types.contains(elementalType) }
                    .map { form -> flatMapSpecies to form }
            }
            .toList()

        if (species.isNotEmpty()) return species

        return allSpecies
            .filter { filterSpecies -> filterSpecies.name !in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies -> associateSpecies.forms.filter { form -> form.name !in CONFIG.ignoredForms!! } }
            .flatMap { (flatMapSpecies, forms) ->
                forms.map { form -> flatMapSpecies to form }
            }
            .toList()
    }

    fun fillPokemonModel(species: Pair<Species, FormData>, level: Int): PokemonProperties {
        var pokeString =
            "${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level}"

        if (Random.nextInt(1, 10) == 1) {
            pokeString = pokeString.plus(" shiny=yes")
        }

        val pokemonProperties: PokemonProperties = pokeString.toProperties()

        // Thanks Ludichat [Cobbreeding project code]
        if (pokemonProperties.form != null) {
            species.first.forms.find { it.formOnlyShowdownId() == pokemonProperties.form }?.run {
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
            poke.heldItem().registryEntry.idAsString,
            poke.aspects
        )
    }

    fun generatePokemon(level: Int, type: String?): PokemonProperties {
        debug("Generating pokemon with level $level and type $type")
        if (type != null && type != "default") {
            val species = SPECIES_BY_TYPE[type]
                ?.toList()
                ?.random()!!
            debug("Picked ${species.first.showdownId()} form=${species.second.formOnlyShowdownId()} level=${level}")

            return fillPokemonModel(species, level)
        } else {
            val species = PokemonSpecies.implemented.asSequence()
                .filter { species -> species.name !in CONFIG.ignoredSpecies!! }
                .filter { species ->
                    species.implemented
                }
                .associateWith { species ->
                    species
                        .forms
                        .filter { form -> form.name !in CONFIG.ignoredForms!! }
                }
                .flatMap { (species, forms) ->
                    forms.map { form -> species to form }
                }
                .random()

            debug("Picked ${species.first.resourceIdentifier.path} form=${species.second.formOnlyShowdownId()} level=${level} from random pool")


            return fillPokemonModel(species, level)
        }
    }


    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
