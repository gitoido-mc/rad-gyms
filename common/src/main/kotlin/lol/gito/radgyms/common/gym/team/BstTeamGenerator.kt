/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms.config
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.registry.RadGymsSpeciesRegistry.speciesByType

object BstTeamGenerator : GenericTeamGenerator() {
    override fun generatePokemon(
        level: Int,
        amount: Int,
        type: ElementalType,
    ): Pokemon {
        debug("Rolling for pokemon with level $level and type ${type.showdownId}")
        val speciesList = speciesByType[type.showdownId]!!

        val levelChunkedRange =
            (config.minLevel!!..config.maxLevel!!)
                .chunked(
                    // This calculates the chunk size between min and max levels defined in config
                    (config.maxLevel!!)
                        .minus(config.minLevel!!)
                        .toDouble()
                        .div(amount)
                        .toInt()
                        .minus(1),
                )

        val derivedChunkIndex = levelChunkedRange.indexOfFirst { it.contains(level) }

        val derivedSpecies =
            speciesList
                .chunked(speciesList.count() / levelChunkedRange.count())
                .let {
                    it[derivedChunkIndex].random()
                }

        debug("Picked {} with {} form", derivedSpecies.species.name, derivedSpecies.form.name)

        return getPokemon(derivedSpecies, level)
    }
}
