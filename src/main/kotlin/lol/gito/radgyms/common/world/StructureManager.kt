/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.world

import lol.gito.radgyms.common.RadGyms.LOGGER
import lol.gito.radgyms.common.RadGyms.debug
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings

object StructureManager {
    fun placeStructure(world: WorldGenLevel, pos: BlockPos, structureId: String) {
        val structureResource = ResourceLocation.parse(structureId)
        val structTemplateManager = world.server?.structureManager
        val structureTemplate = structTemplateManager?.get(structureResource)

        if (structureTemplate != null) {
            val structPlacementData = StructurePlaceSettings()
                .setIgnoreEntities(true)
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE)
                .setKnownShape(true)

            val random = WorldgenRandom(LegacyRandomSource(0L))
            random.setLargeFeatureSeed(world.seed, pos.x shr 4, pos.z shr 4)

            if (!structureTemplate.get().placeInWorld(world, pos, pos, structPlacementData, random, 18)) {
                LOGGER.warn("Error placing structure: $structureResource")
            } else {
                structTemplateManager.remove(structureResource)
                debug("Successfully placed structure: $structureId at ${pos.x},${pos.y},${pos.z}")
            }
        } else {
            LOGGER.warn("Failed to load structure: $structureResource")
        }
    }
}
