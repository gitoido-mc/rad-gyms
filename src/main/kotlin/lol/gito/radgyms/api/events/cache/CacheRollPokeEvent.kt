/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.cache

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Rarity

data class CacheRollPokeEvent(
    val player: ServerPlayerEntity,
    var poke: Pokemon,
    var type: String,
    var rarity: Rarity,
    var shinyBoost: Int
)
