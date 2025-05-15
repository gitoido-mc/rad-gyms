package lol.gito.radgyms.block.type

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HorizontalFacingBlockWithEntity(settings: Settings) : HorizontalFacingBlock(settings), BlockEntityProvider {

    override fun createBlockEntity(
        pos: BlockPos?,
        state: BlockState?
    ): BlockEntity? {
        TODO("Not yet implemented")
    }

    override fun getCodec(): MapCodec<out HorizontalFacingBlock?>? {
        TODO("Not yet implemented")
    }
}
