/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.util

import com.cobblemon.mod.common.Cobblemon
import lol.gito.radgyms.common.RadGyms
import net.minecraft.server.network.ServerPlayerEntity
import kotlin.math.roundToInt

fun ServerPlayerEntity.averagePokePartyLevel(): Int {
    val party = Cobblemon.storage.getParty(this)

    return party.map { it -> it.level }.average().roundToInt().coerceIn(
        RadGyms.CONFIG.minLevel,
        RadGyms.CONFIG.maxLevel
    )
}