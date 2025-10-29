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
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.block.entity.GymEntranceEntity
import lol.gito.radgyms.common.network.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.registry.BlockRegistry.GYM_ENTRANCE
import lol.gito.radgyms.server.util.averagePokePartyLevel
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

private val gymEntranceHorizontalFacing: DirectionProperty = Properties.HORIZONTAL_FACING

class GymEntranceBlock(settings: Settings) : BlockWithEntity(settings) {
    private val bounds = VoxelShapes.union(
        createCuboidShape(3.75, 1.75, 3.75, 12.25, 10.25, 12.25),
        createCuboidShape(6.5, 10.0, 6.5, 9.5, 11.0, 9.5)
    )

    init {
        defaultState = defaultState.with(gymEntranceHorizontalFacing, Direction.NORTH)
    }

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymEntranceEntity(pos, state)

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymEntranceBlock(settings) }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = bounds

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = bounds

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = this.defaultState.with(
        gymEntranceHorizontalFacing,
        ctx.horizontalPlayerFacing.opposite
    )

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.PASS
        if (world.getBlockEntity(pos) !is GymEntranceEntity) return super.onUse(state, world, pos, player, hit)

        val party = Cobblemon.implementation.server()!!.playerManager.getPlayer(player.uuid)!!.party()
        if (party.occupied() == 0) {
            player.sendMessage(translatable(modId("message.info.gym_entrance_party_empty").toTranslationKey()))
            debug("Player ${player.uuid} tried to use $pos gym entry with empty party, denying...")
            return ActionResult.FAIL
        }

        if (party.all { it.isFainted() }) {
            player.sendMessage(translatable(modId("message.info.gym_entrance_party_fainted").toTranslationKey()))
            debug("Player ${player.uuid} tried to use $pos gym entry with party fainted, denying...")
            return ActionResult.FAIL
        }

        val gymEntrance: GymEntranceEntity = world.getBlockEntity(pos) as GymEntranceEntity

        if (gymEntrance.usesLeftForPlayer(player) == 0) {
            player.sendMessage(translatable(modId("message.info.gym_entrance_exhausted").toTranslationKey()))
            debug("Player ${player.uuid} tried to use $pos gym entry with tries exhausted, denying...")
            return ActionResult.FAIL
        }

        ServerPlayNetworking.send(
            player as ServerPlayerEntity,
            OpenGymEnterScreenS2C(
                when (RadGyms.CONFIG.deriveAverageGymLevel!!) {
                    true -> player.averagePokePartyLevel()
                    false -> RadGyms.CONFIG.minLevel!!
                },
                false,
                gymEntrance.gymType,
                pos
            )
        )

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
