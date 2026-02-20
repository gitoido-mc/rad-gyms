/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block

import com.cobblemon.mod.common.util.party
import com.mojang.serialization.MapCodec
import lol.gito.radgyms.common.MIN_PLAYER_TEAM_SIZE
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.extension.averagePokePartyLevel
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.helper.tlr
import lol.gito.radgyms.common.net.server.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.registry.RadGymsBlocks.GYM_ENTRANCE
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

private val gymEntranceHorizontalFacing: DirectionProperty = HORIZONTAL_FACING

class GymEntranceBlock(properties: Properties) : BaseEntityBlock(properties) {
    @Suppress("MagicNumber")
    private val bounds = Shapes.or(
        box(3.75, 1.75, 3.75, 12.25, 10.25, 12.25),
        box(6.5, 10.0, 6.5, 9.5, 11.0, 9.5),
    )

    init {
        registerDefaultState(
            defaultBlockState().setValue(gymEntranceHorizontalFacing, Direction.NORTH),
        )
    }

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec { GymEntranceBlock(it) }

    override fun getShape(
        state: BlockState,
        blockGetter: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = bounds

    override fun getCollisionShape(
        state: BlockState,
        blockGetter: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = bounds

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HORIZONTAL_FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState = this.defaultBlockState().setValue(
        gymEntranceHorizontalFacing,
        ctx.horizontalDirection.opposite,
    )

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult,
    ): InteractionResult {
        if (level.getBlockEntity(pos) !is GymEntranceEntity) return super.useWithoutItem(state, level, pos, player, hit)
        var result: InteractionResult = InteractionResult.SUCCESS_NO_ITEM_USED
        when (level.isClientSide) {
            true -> result = InteractionResult.PASS
            false -> {
                (player as ServerPlayer).let { player ->
                    if (player.party().occupied() < MIN_PLAYER_TEAM_SIZE) {
                        player.displayClientMessage(tl("message.info.gym_entrance_party_empty"))
                        debug("Player ${player.uuid} tried to use $pos gym entry with empty party, denying...")
                        result = InteractionResult.FAIL
                        return@let
                    }

                    if (player.party().all { it.isFainted() }) {
                        player.displayClientMessage(tl("message.info.gym_entrance_party_fainted"))
                        debug("Player ${player.uuid} tried to use $pos gym entry with party fainted, denying...")
                        result = InteractionResult.FAIL
                        return@let
                    }

                    val gymEntrance: GymEntranceEntity = level.getBlockEntity(pos) as GymEntranceEntity

                    if (gymEntrance.usesLeftForPlayer(player) == 0) {
                        player.displayClientMessage(tl("message.info.gym_entrance_exhausted"))
                        debug("Player ${player.uuid} tried to use $pos gym entry with tries exhausted, denying...")
                        result = InteractionResult.FAIL
                        return@let
                    }

                    val derivedLevel =
                        when (RadGyms.config.deriveAverageGymLevel!!) {
                            true -> player.averagePokePartyLevel()
                            false -> RadGyms.config.minLevel!!
                        }

                    OpenGymEnterScreenS2C(
                        derivedLevel,
                        false,
                        gymEntrance.gymType,
                        pos,
                        gymEntrance.usesLeftForPlayer(player),
                    ).sendToPlayer(player)
                }
            }
        }

        return result
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Component>,
        options: TooltipFlag,
    ) {
        tooltip.addLast(tlr("${GYM_ENTRANCE.descriptionId}.tooltip").withStyle(ChatFormatting.GRAY))
        tooltip.addLast(tlr("${GYM_ENTRANCE.descriptionId}.tooltip2").withStyle(ChatFormatting.GRAY))
    }

    override fun setPlacedBy(
        world: Level,
        pos: BlockPos,
        state: BlockState,
        livingEntity: LivingEntity?,
        itemStack: ItemStack,
    ) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack)

        if (!world.isClientSide) {
            (world as ServerLevel).chunkSource.blockChanged(pos)
        }
    }
}
