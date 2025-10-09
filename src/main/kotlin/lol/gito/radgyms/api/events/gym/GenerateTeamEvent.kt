/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.events.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import net.minecraft.server.network.ServerPlayerEntity

data class GenerateTeamEvent(
    val player: ServerPlayerEntity,
    val type: String,
    val level: Int,
    val trainerId: String,
    val isLeader: Boolean,
    val team: MutableList<PokemonProperties>
)
