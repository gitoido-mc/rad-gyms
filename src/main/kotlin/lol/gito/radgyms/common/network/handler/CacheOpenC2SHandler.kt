/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.api.events.ModEvents
import lol.gito.radgyms.common.network.payload.CacheOpenC2S
import lol.gito.radgyms.common.pokecache.CacheHandler
import lol.gito.radgyms.common.registry.EventRegistry.CACHE_ROLL_POKE
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

class CacheOpenC2SHandler(payload: CacheOpenC2S, context: ServerPlayNetworking.Context) {
    init {
        val poke: Pokemon = CacheHandler.getPoke(
            payload.type,
            payload.rarity,
            context.player(),
            payload.shinyBoost
        )

        CACHE_ROLL_POKE.emit(
            ModEvents.CacheRollPokeEvent(
                context.player(),
                poke,
                payload.type,
                payload.rarity,
                payload.shinyBoost
            )
        )
    }
}