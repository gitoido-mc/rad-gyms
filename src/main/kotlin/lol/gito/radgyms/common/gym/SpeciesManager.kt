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
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.GymSpecies
import lol.gito.radgyms.common.pokecache.CacheDTO
import kotlin.random.Random
import kotlin.time.TimeSource.Monotonic.markNow

object SpeciesManager {
    var SPECIES_TIMESTAMP = markNow()
    var SPECIES_BY_TYPE: HashMap<String, List<GymSpecies.Container.SpeciesWithForm>> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: MutableMap<String, CacheDTO> = mutableMapOf()

    fun speciesOfType(elementalType: ElementalType): List<GymSpecies.Container.SpeciesWithForm> {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies
            .filter { filterSpecies -> filterSpecies.resourceIdentifier.path !in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies ->
                associateSpecies.forms.filter { form -> form.formOnlyShowdownId() !in CONFIG.ignoredForms!! }
            }
            .flatMap { (flatMapSpecies, forms) ->
                forms
                    .filter { form -> form.types.contains(elementalType) }
                    .map {
                        GymSpecies.Container.SpeciesWithForm(flatMapSpecies, it)
                    }
            }
            .toList()

        if (species.isNotEmpty()) return species

        return allSpecies
            .filter { filterSpecies -> filterSpecies.resourceIdentifier.path !in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies ->
                associateSpecies.forms.filter { form -> form.formOnlyShowdownId() !in CONFIG.ignoredForms!! }
            }
            .flatMap { (flatMapSpecies, forms) ->
                forms.map {
                    GymSpecies.Container.SpeciesWithForm(flatMapSpecies, it)
                }
            }
            .toList()
    }

    fun fillPokemonModel(derived: GymSpecies.Container.SpeciesWithForm, level: Int): PokemonProperties {
        var pokeString =
            "${derived.species.resourceIdentifier.path} form=${derived.form.name} level=${level}"

        if (Random.nextInt(1, 10) == 1) {
            pokeString = pokeString.plus(" shiny=yes")
        }

        val pokemonProperties: PokemonProperties = pokeString.toProperties()

        // Thanks Ludichat [Cobbreeding project code]
        if (pokemonProperties.form != null) {
            derived.species.standardForm
            derived.species.forms.find { it.formOnlyShowdownId() == pokemonProperties.form }?.run {
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
            poke.heldItem().itemHolder.registeredName,
            poke.aspects
        )
    }

    fun generatePokemon(level: Int, type: String?): PokemonProperties {
        debug("Generating pokemon with level $level and type $type")
        if (type != null && type != "default") {
            val derived = SPECIES_BY_TYPE[type]
                ?.toList()
                ?.random()!!
            debug("Picked ${derived.species.showdownId()} form=${derived.form.formOnlyShowdownId()} level=${level}")

            return fillPokemonModel(derived, level)
        } else {
            val derived = PokemonSpecies.implemented.asSequence()
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
                    forms.map {GymSpecies.Container.SpeciesWithForm(species, it) }
                }
                .random()

            debug("Picked ${derived.species.resourceIdentifier.path} form=${derived.form.formOnlyShowdownId()} level=${level} from random pool")


            return fillPokemonModel(derived, level)
        }
    }


    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
