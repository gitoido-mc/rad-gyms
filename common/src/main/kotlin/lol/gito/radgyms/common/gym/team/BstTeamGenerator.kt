/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.types.ElementalType
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModel
import kotlin.collections.chunked
import kotlin.collections.count
import kotlin.collections.forEach
import kotlin.collections.random

class BstTeamGenerator: AbstractTeamGenerator() {
    override fun generatePokemon(
        level: Int,
        type: ElementalType,
        thresholdAmount: Int
    ): PokemonProperties  {
        debug("Rolling for pokemon with level $level and type ${type.showdownId}")
        val speciesList = SPECIES_BY_TYPE[type.showdownId]!!

        val levelRange = (CONFIG.minLevel!!..CONFIG.maxLevel!!)

        // derive amount of chunks
        val chunkSize = (CONFIG.maxLevel!!)
            .minus(CONFIG.minLevel!!)
            .toDouble()
            .div(thresholdAmount)
            .toInt()
            .minus(1)

        val levelChunkedRange = levelRange
            .chunked(chunkSize)

        val selectedLevelChunkIndex = levelChunkedRange.indexOfFirst { it.contains(level) }

        val chunked = speciesList
            .chunked(speciesList.count() / levelChunkedRange.count())

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
}