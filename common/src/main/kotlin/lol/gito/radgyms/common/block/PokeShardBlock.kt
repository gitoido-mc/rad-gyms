/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block

import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState

open class PokeShardBlockBase(
    val rarity: Rarity,
) : Block(Properties.ofFullCopy(Blocks.LAPIS_BLOCK)) {
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL
}

class EpicShardBlock : PokeShardBlockBase(rarity = Rarity.EPIC)

class RareShardBlock : PokeShardBlockBase(rarity = Rarity.RARE)

class UncommonShardBlock : PokeShardBlockBase(rarity = Rarity.UNCOMMON)

class CommonShardBlock : PokeShardBlockBase(rarity = Rarity.COMMON)
