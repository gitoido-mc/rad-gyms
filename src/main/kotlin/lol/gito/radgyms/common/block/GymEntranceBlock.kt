/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.block

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.util.party
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.client.gui.screen.GymEnterScreen
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.registry.BlockRegistry.GYM_ENTRANCE
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class GymEntranceBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymEntranceBlock(settings) }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.getBlockEntity(pos) !is GymEntranceEntity) return super.onUse(state, world, pos, player, hit)

        if (!world.isClient) {
            val party = Cobblemon.implementation.server()!!.playerManager.getPlayer(player.uuid)!!.party()
            if (party.occupied() == 0) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_party_empty").toTranslationKey()))
                debug("Player ${player.uuid} tried to use $pos gym entry with empty party, denying...")
                return ActionResult.FAIL
            }

            party.forEach { it.heal() }

            if (party.all { it.isFainted() }) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))
                debug("Player ${player.uuid} tried to use $pos gym entry with party fainted, denying...")
                return ActionResult.FAIL
            }
        }

        val gymEntrance: GymEntranceEntity = world.getBlockEntity(pos) as GymEntranceEntity

        if (gymEntrance.usesLeftForPlayer(player) == 0) {
            if (!world.isClient) player.sendMessage(translatable(modId("message.info.gym_entrance_exhausted").toTranslationKey()))
            debug("Player ${player.uuid} tried to use $pos gym entry with tries exhausted, denying...")
            return ActionResult.FAIL
        }

        if (world.isClient) {
            debug(
                "Client: Opening gym entry screen for player ${player.uuid} (tries left: ${
                    gymEntrance.usesLeftForPlayer(
                        player
                    )
                })"
            )


            MinecraftClient.getInstance().setScreen(
                GymEnterScreen(true, gymEntrance.gymType)
            )
//            GuiHandler.openGymEntranceScreen(player, gymEntrance.gymType, pos, gymEntrance.usesLeftForPlayer(player))
        }

        return ActionResult.SUCCESS
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType
    ) {
        tooltip.addLast(translatable(GYM_ENTRANCE.translationKey.plus(".tooltip")).formatted(Formatting.GRAY))
        tooltip.addLast(translatable(GYM_ENTRANCE.translationKey.plus(".tooltip2")).formatted(Formatting.GRAY))
    }

    override fun onPlaced(
        world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)

        if (!world.isClient) {
            (world as ServerWorld).chunkManager.markForUpdate(pos)
        }
    }
}
