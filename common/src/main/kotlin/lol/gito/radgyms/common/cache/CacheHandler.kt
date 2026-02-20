/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.cache

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.exception.RadGymsPoolNotDefinedException
import lol.gito.radgyms.common.extension.shinyRoll
import lol.gito.radgyms.common.registry.RadGymsSpeciesRegistry.speciesByRarity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Rarity

object CacheHandler {
    fun getPoke(
        type: String,
        rarity: Rarity,
        player: ServerPlayer,
        shinyBoost: Int = 0,
    ): Pokemon =
        getPoke(
            ElementalTypes.get(type) ?: ElementalTypes.all().random(),
            rarity,
            player,
            shinyBoost,
        )

    fun getPoke(
        type: ElementalType,
        rarity: Rarity,
        player: ServerPlayer,
        shinyBoost: Int = 0,
    ): Pokemon {
        /**
         * [speciesByRarity] holds all species in map-like object
         * where the key corresponds to [ElementalType.showdownId] property of passed [type]
         * [CacheDTO.forRarity] is a function that returns shuffleable list of species
         */
        val pool =
            speciesByRarity[type.showdownId] ?: throw RadGymsPoolNotDefinedException(
                "Cannot find pool: ${rarity.serializedName.lowercase()}",
            )

        val cache = pool.forRarity(rarity)
        var poke: Pokemon
        do {
            val pokeProps = PokemonProperties.parse(cache.shuffle().first())
            poke = pokeProps.create()
        } while (!poke.species.implemented)

        shinyRoll(poke, player, shinyBoost)
        poke.updateAspects()
        poke.updateForm()

        val instance = poke.initialize()

        return instance
    }
}
