/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.extension

import com.cobblemon.mod.common.Cobblemon
import lol.gito.radgyms.common.RadGyms
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import kotlin.math.roundToInt

fun ServerPlayer.averagePokePartyLevel(): Int {
    val party = Cobblemon.storage.getParty(this)

    return party.map { it.level }.average().roundToInt().coerceIn(
        RadGyms.CONFIG.minLevel,
        RadGyms.CONFIG.maxLevel
    )
}


fun Player.displayClientMessage(component: Component, maybeOverlay: Boolean = true) =
    this.displayClientMessage(component, maybeOverlay)
