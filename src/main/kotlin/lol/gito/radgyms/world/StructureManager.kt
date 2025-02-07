package lol.gito.radgyms.world

import lol.gito.radgyms.RadGyms
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
                RadGyms.LOGGER.info("Error placing structure: $structureId")
            } else {
                structTemplateManager.unloadTemplate(Identifier.of(structureId))
                RadGyms.LOGGER.info("Successfully placed structure: $structureId at ${pos.x},${pos.y},${pos.z}")
            }
        } else {
            RadGyms.LOGGER.info("Failed to load structure: $structureId")
        }
    }
}