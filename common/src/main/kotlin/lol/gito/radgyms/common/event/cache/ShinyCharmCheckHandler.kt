/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.cache

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.util.shinyRoll
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey

class ShinyCharmCheckHandler(event: GymEvents.CacheRollPokeEvent) {
    init {
        var hasShinyCharm = event.player.inventory.contains(
            TagKey.create(
                Registries.ITEM,
                modId("items/shiny_chance_items")
            )
        )

        if (!event.poke.shiny && hasShinyCharm) {
            shinyRoll(event.poke, event.player, event.shinyBoost)
        }
    }
}