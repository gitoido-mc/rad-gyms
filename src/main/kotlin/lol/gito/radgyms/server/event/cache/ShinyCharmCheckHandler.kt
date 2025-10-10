/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.event.cache

import lol.gito.radgyms.api.events.cache.CacheRollPokeEvent
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.server.util.isShiny
import lol.gito.radgyms.server.util.shinyRoll
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

class ShinyCharmCheckHandler(event: CacheRollPokeEvent) {
    init {
        var hasShinyCharm = event.player.inventory.contains(
            TagKey.of(
                RegistryKeys.ITEM,
                modId("items/shiny_chance_items")
            )
        )

        if (!event.poke.shiny && hasShinyCharm) {
            event.poke.shiny = shinyRoll(event.poke, event.player, event.shinyBoost).isShiny()
        }
    }
}