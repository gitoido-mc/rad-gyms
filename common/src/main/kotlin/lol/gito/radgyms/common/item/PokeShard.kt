/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.item

import com.cobblemon.mod.common.item.CobblemonItem
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

open class PokeShardBase(private val rarity: Rarity) : CobblemonItem(
    Properties().rarity(rarity)
) {
    override fun getDefaultInstance(): ItemStack = super.defaultInstance.also { stack ->
        stack.set(DataComponents.RARITY, this.rarity)
    }
}

class EpicPokeShard : PokeShardBase(Rarity.EPIC)

class RarePokeShard : PokeShardBase(Rarity.RARE)

class UncommonPokeShard : PokeShardBase(Rarity.UNCOMMON)

class CommonPokeShard : PokeShardBase(Rarity.COMMON)
