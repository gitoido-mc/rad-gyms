package lol.gito.radgyms.event

import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.world.dimension.DimensionManager
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.impl.event.interaction.InteractionEventsRouter
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object GymDimensionEvents {
    fun register() {
        PlayerBlockBreakEvents.BEFORE.register(::preventBlockBreakInGymDimension)
        UseBlockCallback.EVENT.register(::preventBlockPlaceInGymDimension)
    }

    private fun preventBlockPlaceInGymDimension(
        playerEntity: PlayerEntity,
        world: World,
        hand: Hand,
        result: BlockHitResult,
    ): ActionResult {
        if (world.registryKey == DimensionManager.RADGYMS_LEVEL_KEY) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    private fun preventBlockBreakInGymDimension(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        entity: BlockEntity?
    ): Boolean {
        if (world.registryKey == DimensionManager.RADGYMS_LEVEL_KEY) {
            return false;
        }

        if (state.block == BlockRegistry.GYM_ENTRANCE) {
            var allowBreak = false
            if (player.isSneaking) {
                player.sendMessage(Text.translatable("radgyms.message.error.gym_entrance.not-sneaking"))
                allowBreak = true
            }
            if (!allowBreak) {
                player.sendMessage(Text.translatable("radgyms.message.info.gym_entrance_breaking"))
            }
            return allowBreak

        }

        return true;
    }
}