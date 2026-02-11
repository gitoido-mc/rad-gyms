/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.cache

import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.util.displayClientMessage
import lol.gito.radgyms.common.util.rainbow
import net.minecraft.network.chat.Component.translatable

class CacheRollPokeHandler(event: GymEvents.CacheRollPokeEvent) {
    init {
        event.player.party().add(event.poke)

        event.player.displayClientMessage(
            translatable(
                modId("message.info.poke_cache.reward").toLanguageKey(),
                translatable(modId("label.rarity.${event.rarity.toString().lowercase()}").toLanguageKey()).withStyle(
                    event.rarity.color()
                ),
                when (event.poke.shiny) {
                    true -> event.poke.species.translatedName.rainbow()
                    false -> event.poke.species.translatedName
                }
            )
        )
        event.player.mainHandItem.consume(1, event.player)
    }
}
