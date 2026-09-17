/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.compat

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation

interface StructurePlacerImplementation {
    fun initialize(
        world: WorldGenLevel,
        templateName: ResourceLocation,
        blockPos: BlockPos,
        mirror: Mirror = Mirror.NONE,
        rotation: Rotation = Rotation.NONE,
        ignoreEntities: Boolean = false,
        integrity: Float = 1f,
        offset: BlockPos = BlockPos.ZERO,
    ): StructurePlacerImplementation

    fun loadStructure(): Boolean

    fun loadAndRestoreStructure(restoreTicks: Int): Boolean

    fun loadAndRestoreStructureAnimated(restoreTicks: Int, blocksPerTick: Int, random: Boolean): Boolean

    fun setReplaceBedrock(replaceBedrock: Boolean)

    fun setReplaceBarrier(replaceBarrier: Boolean)

    fun setOnlyReplaceTaggedBlocks(onlyReplaceTaggedBlocks: Boolean, tag: TagKey<Block>)

    fun setPreventReplacementOfTaggedBlocks(preventReplacementOfTaggedBlocks: Boolean, tag: TagKey<Block>)
}
