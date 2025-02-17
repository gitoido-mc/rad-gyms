package lol.gito.radgyms.block

import com.cobblemon.mod.common.util.party
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import lol.gito.radgyms.gui.GuiHandler
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class GymEntranceBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymEntranceBlock(settings) }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun onPlaced(
        world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)

        if (!world.isClient) {
            (world as ServerWorld).chunkManager.markForUpdate(pos)
        }
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hit: BlockHitResult
    ): ActionResult {
        if (world.getBlockEntity(pos) !is GymEntranceEntity) {
            return super.onUse(state, world, pos, player, hit)
        }

        if (!world.isClient) {
            val party = (player as ServerPlayerEntity).party()
            if (party.all { it.isFainted() }) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))

                return ActionResult.PASS
            }
        }

        val gymEntrance: GymEntranceEntity = world.getBlockEntity(pos) as GymEntranceEntity

        if (gymEntrance.usesLeftForPlayer(player) == 0) {
            if (!world.isClient) player.sendMessage(translatable(modId("message.info.gym_entrance_exhausted").toTranslationKey()))

            return ActionResult.PASS
        }

        if (world.isClient) {
            GuiHandler.openGymEntranceScreen(player, gymEntrance.gymType, pos, gymEntrance.usesLeftForPlayer(player))
        }

        return ActionResult.SUCCESS
    }
}