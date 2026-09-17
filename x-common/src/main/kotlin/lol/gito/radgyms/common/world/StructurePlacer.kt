/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.world

import lol.gito.radgyms.common.api.compat.StructurePlacerImplementation
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.WorldGenLevel
import java.util.ServiceLoader

object StructurePlacer {
    val structurePlacer: StructurePlacerImplementation

    init {
        with(ServiceLoader.load(StructurePlacerImplementation::class.java).findFirst()) {
            if (this.isEmpty) {
                throw RuntimeException("Cannot load structure placer service")
            }
            this@StructurePlacer.structurePlacer = this.get()
        }
    }

    fun placeStructure(world: WorldGenLevel, pos: BlockPos, structureId: String): Boolean {
        val structureResource = ResourceLocation.parse(structureId)
        structurePlacer.initialize(world, structureResource, pos)
        return structurePlacer.loadStructure()
    }
}
