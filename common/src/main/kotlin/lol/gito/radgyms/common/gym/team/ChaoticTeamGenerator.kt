/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalType
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModel

class ChaoticTeamGenerator: AbstractTeamGenerator() {
    override fun generatePokemon(
        level: Int,
        type: ElementalType,
        thresholdAmount: Int
    ): PokemonProperties = fillPokemonModel(SPECIES_BY_TYPE[type.showdownId]!!.random(), level)
}