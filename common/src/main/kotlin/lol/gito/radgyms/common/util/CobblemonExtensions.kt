/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.RadGyms
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

fun shinyRoll(poke: Pokemon, player: ServerPlayer, shinyBoost: Int? = 0) {
    var shinyRate = Cobblemon.config.shinyRate
    val event = ShinyChanceCalculationEvent(shinyRate, poke)

    // adding negative modifier increases the chances by lowering the base chance
    // adding positive modifier decreases the chances instead
    event.addModifier(-(shinyBoost ?: 0).toFloat())
    CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
        RadGyms.LOGGER.info("Checking shiny for cache used by ${player.uuid}")
        shinyRate = event.calculate(player)
        RadGyms.LOGGER.info("Derived rate is $shinyRate")
    }

    // yoinked from cobblemon, it was private there
    poke.shiny = (shinyRate > 0 && (Random.nextFloat() < 1 / shinyRate))
}