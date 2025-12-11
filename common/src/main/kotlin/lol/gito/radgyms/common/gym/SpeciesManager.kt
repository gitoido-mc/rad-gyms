/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.util.toProperties
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.dto.GymSpecies
import lol.gito.radgyms.common.pokecache.CacheDTO
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.time.TimeSource.Monotonic.markNow

object SpeciesManager {
    var SPECIES_TIMESTAMP = markNow()
    var SPECIES_BY_TYPE: HashMap<String, List<GymSpecies.Container.SpeciesWithForm>> = HashMap(ElementalTypes.count())
    var SPECIES_BY_RARITY: Map<String, CacheDTO> = mutableMapOf()

    fun speciesOfType(elementalType: ElementalType): List<GymSpecies.Container.SpeciesWithForm> {
        val allSpecies = PokemonSpecies.implemented.asSequence()
        val species = allSpecies
            .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
            .associateWith { associateSpecies ->
                associateSpecies.forms.filterNot { it.formOnlyShowdownId() !in CONFIG.ignoredForms!! }
            }
            .flatMap { (flatMapSpecies, forms) ->
                forms
                    .filter { form -> form.types.contains(elementalType) }
                    .map {
                        GymSpecies.Container.SpeciesWithForm(flatMapSpecies, it)
                    }
            }
            .toList()
            .sortedBy {
                it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
            }

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
            .sortedBy {
                it.form.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
            }
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

    fun generatePokemon(level: Int, type: String, thresholdAmount: Int): PokemonProperties {
        debug("Rolling for pokemon with level $level and type $type")
        val speciesList = when (type) {
            "default" -> PokemonSpecies.implemented.asSequence()
                .filterNot { it.resourceIdentifier.path in CONFIG.ignoredSpecies!! }
                .filter { it.implemented }
                .associateWith { species ->
                    species
                        .forms
                        .filterNot { it.formOnlyShowdownId() in CONFIG.ignoredForms!! }
                }
                .flatMap { (species, forms) ->
                    forms.map { GymSpecies.Container.SpeciesWithForm(species, it) }
                }

            else -> SPECIES_BY_TYPE[type]!!
        }

        val chunkSize = (CONFIG.maxLevel!!)
                .minus(CONFIG.minLevel!!)
                .toDouble()
                .div(thresholdAmount)
                .let { ceil(it).toInt() }

        val levelChunkedRange = (CONFIG.minLevel!!..CONFIG.maxLevel!!)
            .chunked(chunkSize)
        val selectedLevelChunkIndex = levelChunkedRange.indexOfFirst { it.contains(level) }
        val chunked = speciesList
            .chunked(ceil(speciesList.count().toDouble() / chunkSize).toInt())

        val derived = chunked[selectedLevelChunkIndex].random()

        debug("BST chunks: $levelChunkedRange")
        chunked.forEachIndexed { index, forms ->
            debug("Species in chunk {}: {}", index, forms.count())
        }
        debug("Selected chunk index: $selectedLevelChunkIndex")
        debug("Species in BST chunk: ${chunked[selectedLevelChunkIndex].count()}")
        chunked[selectedLevelChunkIndex].forEach { debug(
            "Species: {}; Form: {}; BST: {}",
            it.species.resourceIdentifier.path,
            it.form.formOnlyShowdownId(),
            it.species.baseStats.filterKeys { key -> key.type == Stat.Type.PERMANENT }.values.sum()
        ) }
        debug("Picked ${derived.species.showdownId()} form=${derived.form.formOnlyShowdownId()} level=${level}")

        return fillPokemonModel(derived, level)
    }


    fun register() {
        debug("Initializing SpeciesManager instance")
    }
}
