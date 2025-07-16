/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.cache

import kotlinx.serialization.Serializable
import net.minecraft.util.Rarity
import net.minecraft.util.collection.WeightedList

@Serializable
class CacheDTO(
    val common: HashMap<String, Int>,
    val uncommon: HashMap<String, Int>,
    val rare: HashMap<String, Int>,
    val epic: HashMap<String, Int>,
) {
    fun forRarity(rarity: Rarity): WeightedList<String> {
        val pokeList = when (rarity) {
            Rarity.COMMON -> this.common
            Rarity.UNCOMMON -> this.uncommon + this.common
            Rarity.RARE -> this.rare + this.uncommon + this.common
            Rarity.EPIC -> this.epic + this.rare + this.uncommon
        }
        val list = WeightedList<String>()
        for (pokeItem in pokeList) {
            if (list.none { it -> it == pokeItem.key }) {
                list.add(pokeItem.key, pokeItem.value)
            }
        }
        return list
    }
}
