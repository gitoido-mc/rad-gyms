/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.pokecache

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_RARITY
import lol.gito.radgyms.util.isShiny
import lol.gito.radgyms.util.shinyRoll
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Rarity

object CacheHandler {
    fun getPoke(
        type: String,
        rarity: Rarity,
        player: ServerPlayerEntity,
        shinyBoost: Int? = 0
    ): Pokemon = getPoke(
        ElementalTypes.get(type) ?: ElementalTypes.all().random(),
        rarity,
        player,
        shinyBoost
    )

    fun getPoke(
        type: ElementalType,
        rarity: Rarity,
        player: ServerPlayerEntity,
        shinyBoost: Int? = 0
    ): Pokemon {
        val cache = SPECIES_BY_RARITY[type.name]!!.forRarity(rarity)

        val pokeProps = PokemonProperties.parse(cache.shuffle().first())
        val poke = pokeProps.create()

        poke.shiny = shinyRoll(poke, player, shinyBoost).isShiny()

        poke.updateAspects()
        poke.updateForm()

        val instance = poke.initialize()

        return instance
    }

}
