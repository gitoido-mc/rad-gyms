/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.cache

import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.extension.rainbow
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.item.PokeCache
import lol.gito.radgyms.common.registry.RadGymsStats.getStat

class CacheRollPokeHandler(event: GymEvents.CacheRollPokeEvent) {
    init {
        event.player.party().add(event.poke)

        event.player.displayClientMessage(
            tl(
                "message.info.poke_cache.reward",
                tl("label.rarity.${event.rarity.toString().lowercase()}").withStyle(event.rarity.color()),
                when (event.poke.shiny) {
                    true ->
                        event.poke.species.translatedName
                            .rainbow()

                    false -> event.poke.species.translatedName
                },
            ),
        )

        if (event.player.mainHandItem.item is PokeCache) {
            event.player.mainHandItem.consume(1, event.player)
        }
        event.player.awardStat(getStat(RadGyms.statistics.CACHES_OPENED))
    }
}
