package lol.gito.radgyms.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class GymExitEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(
        BlockEntityRegistry.GYM_EXIT_ENTITY,
        pos,
        state
    )
