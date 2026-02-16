/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.block

import com.mojang.serialization.MapCodec
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.block.entity.GymExitEntity
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.net.server.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class GymExitBlock(properties: Properties) : BaseEntityBlock(properties) {
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun codec(): MapCodec<out BaseEntityBlock> =
        simpleCodec { properties: Properties -> GymExitBlock(properties) }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymExitEntity(pos, state)

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult
    ): InteractionResult {
        if (level.getBlockEntity(pos) !is GymExitEntity) return super.useWithoutItem(state, level, pos, player, hit)
        var result: InteractionResult = InteractionResult.SUCCESS_NO_ITEM_USED
        when (level.isClientSide) {
            true -> result = InteractionResult.PASS
            false -> {
                (player as ServerPlayer).also {
                    debug("Gym exit block used by player ${it.uuid} at $pos in ${level.dimension()}")

                    if (level.dimension() == RadGymsDimensions.GYM_DIMENSION) {
                        debug("Client: Opening gym exit screen for ${it.uuid} at $pos in ${level.dimension()}")
                        OpenGymLeaveScreenS2C().sendToPlayer(it)
                    } else {
                        it.displayClientMessage(tl("${EXIT_ROPE.descriptionId}.failed"))
                    }
                }
            }
        }

        return result
    }
}
