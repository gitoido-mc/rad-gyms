/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.event.cache

import com.cobblemon.mod.common.util.party
import lol.gito.radgyms.api.events.cache.CacheRollPokeEvent
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.util.rainbow
import net.minecraft.text.Text.translatable

class CacheRollPokeHandler(event: CacheRollPokeEvent) {
    init {
        event.player.party().add(event.poke)

        event.player.sendMessage(
            translatable(
                modId("message.info.poke_cache.reward").toTranslationKey(),
                translatable(modId("label.rarity.${event.rarity.toString().lowercase()}").toTranslationKey()).formatted(
                    event.rarity.formatting
                ),
                when (event.poke.shiny) {
                    true -> event.poke.species.translatedName.rainbow()
                    false -> event.poke.species.translatedName
                }
            )
        )
        event.player.mainHandStack.decrementUnlessCreative(1, event.player)
    }
}