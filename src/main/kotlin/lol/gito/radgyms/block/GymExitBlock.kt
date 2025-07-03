package lol.gito.radgyms.block

import com.mojang.serialization.MapCodec
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.block.entity.GymExitEntity
import lol.gito.radgyms.client.gui.GuiHandler
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
    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymExitBlock(settings) }

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymExitEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        debug("Gym exit block used by player ${player.uuid} at $pos in ${world.registryKey}")
        if (world.isClient) {
            debug("Client: Opening gym exit screen for ${player.uuid} at $pos in ${world.registryKey}")
            GuiHandler.openGymLeaveScreen(player)
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}
