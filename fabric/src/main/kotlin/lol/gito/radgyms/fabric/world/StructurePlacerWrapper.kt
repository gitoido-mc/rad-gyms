/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.world

import lol.gito.radgyms.common.api.compat.StructurePlacerImplementation
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation

class StructurePlacerWrapper : StructurePlacerImplementation {
    private lateinit var structurePlacer: StructurePlacerAPI

    override fun initialize(
        world: WorldGenLevel,
        templateName: ResourceLocation,
        blockPos: BlockPos,
        mirror: Mirror,
        rotation: Rotation,
        ignoreEntities: Boolean,
        integrity: Float,
        offset: BlockPos,
    ): StructurePlacerImplementation {
        structurePlacer = StructurePlacerAPI(
            world,
            templateName,
            blockPos,
            mirror,
            rotation,
            ignoreEntities,
            integrity,
            offset,
        )

        return this
    }

    override fun loadStructure() = structurePlacer.loadStructure()

    override fun loadAndRestoreStructure(restoreTicks: Int) = structurePlacer.loadAndRestoreStructure(restoreTicks)

    override fun loadAndRestoreStructureAnimated(
        restoreTicks: Int,
        blocksPerTick: Int,
        random: Boolean,
    ) = structurePlacer.loadAndRestoreStructureAnimated(restoreTicks, blocksPerTick, random)

    override fun setReplaceBedrock(replaceBedrock: Boolean) {
        structurePlacer.isReplaceBedrock = true
    }

    override fun setReplaceBarrier(replaceBarrier: Boolean) {
        structurePlacer.isReplaceBarrier = true
    }

    override fun setOnlyReplaceTaggedBlocks(
        onlyReplaceTaggedBlocks: Boolean,
        tag: TagKey<Block>,
    ) {
        structurePlacer.setOnlyReplaceTaggedBlocks(onlyReplaceTaggedBlocks, tag)
    }

    override fun setPreventReplacementOfTaggedBlocks(
        preventReplacementOfTaggedBlocks: Boolean,
        tag: TagKey<Block>,
    ) {
        structurePlacer.setPreventReplacementOfTaggedBlocks(preventReplacementOfTaggedBlocks, tag)
    }
}
