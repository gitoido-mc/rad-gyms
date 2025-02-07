package lol.gito.radgyms.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class GymExitEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(
    BlockEntityRegistry.GYM_ENTRANCE_ENTITY,
    pos,
    state
) {

}