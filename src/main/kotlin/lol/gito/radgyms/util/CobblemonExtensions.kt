/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.util

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.random.Random

fun Float.isShiny(): Boolean =
    if (this >= 1) (Random.Default.nextFloat() < 1 / this) else Random.Default.nextFloat() < this

fun shinyRoll(poke: Pokemon, player: ServerPlayerEntity, shinyBoost: Int? = 0): Float {
    var shinyRate = Cobblemon.config.shinyRate - (shinyBoost ?: 0).toFloat()
    val event = ShinyChanceCalculationEvent(shinyRate, poke)
    CobblemonEvents.SHINY_CHANCE_CALCULATION.post(event) {
        shinyRate = it.calculate(player)
    }
    if (shinyRate == 0f) return 1f
    return shinyRate
}