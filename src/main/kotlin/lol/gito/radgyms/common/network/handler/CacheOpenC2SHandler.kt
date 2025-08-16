/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.handler

import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.network.payload.CacheOpenC2S
import lol.gito.radgyms.common.pokecache.CacheHandler
import lol.gito.radgyms.common.util.TranslationUtil.buildSuffixedTypeText
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class CacheOpenC2SHandler(payload: CacheOpenC2S, context: ServerPlayNetworking.Context) {
    init {
        val poke: Pokemon = CacheHandler.getPoke(
            payload.type,
            payload.rarity,
            context.player(),
            payload.shinyBoost,
            addToParty = true
        )
        context.player().sendMessage(
            Text.of(
                "Rolled %1s %2s %3s%4s".format(
                    payload.rarity.toString().lowercase(),
                    buildSuffixedTypeText(payload.type),
                    when (poke.shiny) {
                        true -> Text.literal("SHINY ").formatted(Formatting.GOLD)
                        false -> Text.literal("")
                    },
                    poke.species.name
                )
            )
        )
        RadGyms.LOGGER.info("shiny boost: ${payload.shinyBoost}")
        context.player().mainHandStack.decrementUnlessCreative(1, context.player())
    }
}