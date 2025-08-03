/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.pokecache.CacheHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.literal
import net.minecraft.util.Rarity

object CacheOpenPacketHandler {
    operator fun invoke(player: ServerPlayerEntity, type: ElementalType, rarity: Rarity, shinyBoost: Int) {
        val poke: Pokemon = CacheHandler.getPoke(type, rarity, player, shinyBoost, addToParty = true)
        player.sendMessage(literal("Rolled $rarity $type ${poke.species.name} shiny: ${poke.shiny}"))
        RadGyms.LOGGER.info("shiny boost: $shinyBoost")
        player.mainHandStack.decrementUnlessCreative(1, player)
    }
}
