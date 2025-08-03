/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.Rarity

open class PokeShardBlockBase(val rarity: Rarity) : Block(Settings.copy(Blocks.LAPIS_BLOCK)) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL
}

class EpicShardBlock : PokeShardBlockBase(rarity = Rarity.EPIC)

class RareShardBlock : PokeShardBlockBase(rarity = Rarity.RARE)

class UncommonShardBlock : PokeShardBlockBase(rarity = Rarity.UNCOMMON)

class CommonShardBlock : PokeShardBlockBase(rarity = Rarity.COMMON)