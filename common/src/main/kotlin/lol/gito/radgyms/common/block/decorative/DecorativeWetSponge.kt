/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block.decorative

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.WetSpongeBlock
import net.minecraft.world.level.block.state.BlockState

class DecorativeWetSponge : WetSpongeBlock(Properties.ofFullCopy(Blocks.WET_SPONGE)) {
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.MODEL
    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, randomSource: RandomSource) = Unit
    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, additional: BlockState, bl: Boolean) = Unit
}
