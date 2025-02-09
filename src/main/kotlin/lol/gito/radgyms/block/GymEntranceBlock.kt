package lol.gito.radgyms.block

import com.mojang.serialization.MapCodec
import lol.gito.radgyms.block.entity.GymEntranceEntity
import lol.gito.radgyms.gui.GuiHandler
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class GymEntranceBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return createCodec { settings: Settings -> GymEntranceBlock(settings) }
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GymEntranceEntity(pos, state)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.getBlockEntity(pos) !is GymEntranceEntity) {
            return super.onUse(state, world, pos, player, hit)
        }

        if (world.isClient) {
             val gymEntrance: GymEntranceEntity = world.getBlockEntity(pos) as GymEntranceEntity
             GuiHandler.openGymEntranceScreen(player, gymEntrance.gymType, pos)
        }

        return ActionResult.SUCCESS
    }
}