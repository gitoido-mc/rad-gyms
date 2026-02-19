/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.cache

import kotlinx.serialization.Serializable
import lol.gito.radgyms.common.exception.RadGymsPoolNotDefinedException
import net.minecraft.world.entity.ai.behavior.ShufflingList
import net.minecraft.world.item.Rarity

typealias CachePoolMap = Map<String, Map<String, Int>>

@Serializable
class CacheDTO(
    val pools: CachePoolMap
) {
    fun forRarity(rarity: Rarity): ShufflingList<String> {
        val pokeList = pools[rarity.serializedName.lowercase()] ?: throw RadGymsPoolNotDefinedException(
            "Cannot find pool: ${rarity.serializedName.lowercase()}"
        )
        val list = ShufflingList<String>()

        for (pokeItem in pokeList) {
            if (list.none { it == pokeItem.key }) {
                list.add(pokeItem.key, pokeItem.value)
            }
        }

        return list
    }
}
