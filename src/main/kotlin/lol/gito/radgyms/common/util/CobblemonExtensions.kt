/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.util

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer
import kotlin.random.Random

fun Float.isShiny(): Boolean =
    if (this >= 1) (Random.nextFloat() < 1 / this) else Random.nextFloat() < this

fun shinyRoll(poke: Pokemon, player: ServerPlayer, shinyBoost: Int? = 0): Float {
    var shinyRate = Cobblemon.config.shinyRate - (shinyBoost ?: 0).toFloat()
    val event = ShinyChanceCalculationEvent(shinyRate, poke)
    CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
        shinyRate = it.calculate(player)
    }
    if (shinyRate == 0f) return 1f
    return shinyRate
}