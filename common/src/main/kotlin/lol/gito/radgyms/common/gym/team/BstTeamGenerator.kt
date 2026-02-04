/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE

class BstTeamGenerator : GenericTeamGenerator() {
    override fun generatePokemon(level: Int, thresholdAmount: Int, type: ElementalType): Pokemon {
        debug("Rolling for pokemon with level $level and type ${type.showdownId}")
        val speciesList = SPECIES_BY_TYPE[type.showdownId]!!

        val levelChunkedRange = (CONFIG.minLevel!!..CONFIG.maxLevel!!)
            .chunked(
                // This calculates the chunk size between min and max levels defined in config
                (CONFIG.maxLevel!!)
                    .minus(CONFIG.minLevel!!)
                    .toDouble()
                    .div(thresholdAmount)
                    .toInt()
                    .minus(1)
            )

        val derivedChunkIndex = levelChunkedRange.indexOfFirst { it.contains(level) }

        val derivedSpecies = speciesList
            .chunked(speciesList.count() / levelChunkedRange.count())
            .let {
                it[derivedChunkIndex].random()
            }

        return getPokemon(derivedSpecies, level)
    }
}