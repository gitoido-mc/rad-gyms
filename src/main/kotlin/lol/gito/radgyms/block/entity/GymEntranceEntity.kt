package lol.gito.radgyms.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class GymEntranceEntity(type: BlockEntityType<GymEntranceEntity>, pos: BlockPos, state: BlockState) :
    BlockEntity(type, pos, state) {
    companion object {

    }
}