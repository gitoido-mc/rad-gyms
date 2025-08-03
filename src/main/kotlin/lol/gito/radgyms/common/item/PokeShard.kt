/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Rarity

class EpicPokeShard : PokeShardBase(Rarity.EPIC)

class RarePokeShard : PokeShardBase(Rarity.RARE)

class UncommonPokeShard : PokeShardBase(Rarity.UNCOMMON)

class CommonPokeShard : PokeShardBase(Rarity.COMMON)

open class PokeShardBase(private val rarity: Rarity) : Item(
    Settings().rarity(rarity)
) {
    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.set(DataComponentTypes.RARITY, this.rarity)

        return stack
    }
}
