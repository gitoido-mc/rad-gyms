package lol.gito.radgyms.block

import com.mojang.serialization.MapCodec
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.block.entity.GymExitEntity
import lol.gito.radgyms.gui.GuiHandler
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class GymExitBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec { settings: Settings -> GymExitBlock(settings) }
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GymExitEntity(pos, state)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        RadGyms.LOGGER.info("exit block used")
        if (world.isClient) {
            GuiHandler.openGymLeaveScreen(player)
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}