package lol.gito.radgyms.world

import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
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

            if (!structureTemplate.get().place(world, pos, pos, structPlacementData, null, 18)) {
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
