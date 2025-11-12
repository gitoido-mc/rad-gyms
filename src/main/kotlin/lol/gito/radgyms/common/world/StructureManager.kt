/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.world

import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.CheckedRandom
import net.minecraft.util.math.random.ChunkRandom
import net.minecraft.world.StructureWorldAccess

object StructureManager {
    fun placeStructure(world: StructureWorldAccess, pos: BlockPos, structureId: String) {
        val structTemplateManager = world.server?.structureTemplateManager
        val structureTemplate = structTemplateManager?.getTemplate(Identifier.of(structureId))

        if (structureTemplate != null) {
            val structPlacementData = StructurePlacementData()
                .setIgnoreEntities(true)
                .setMirror(BlockMirror.NONE)
                .setRotation(BlockRotation.NONE)
                .setUpdateNeighbors(true)

            val random = ChunkRandom(CheckedRandom(0L))
            random.setCarverSeed(world.seed, pos.x shr 4, pos.z shr 4)

            if (!structureTemplate.get().place(world, pos, pos, structPlacementData, random, 18)) {
                LOGGER.warn("Error placing structure: ${Identifier.of(structureId)}")
            } else {
                structTemplateManager.unloadTemplate(Identifier.of(structureId))
                debug("Successfully placed structure: $structureId at ${pos.x},${pos.y},${pos.z}")
            }
        } else {
            LOGGER.warn("Failed to load structure: ${Identifier.of(structureId)}")
        }
    }
}
