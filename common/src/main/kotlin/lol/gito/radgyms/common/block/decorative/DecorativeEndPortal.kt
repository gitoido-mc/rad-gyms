/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block.decorative

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor

class DecorativeEndPortal : Block(
    Properties.ofFullCopy(Blocks.COBBLESTONE)
        .mapColor(MapColor.COLOR_BLACK)
        .lightLevel {
            @Suppress("MagicNumber")
            15
        }
) {
    override fun getRenderShape(blockState: BlockState): RenderShape = RenderShape.MODEL
}
