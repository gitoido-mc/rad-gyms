/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.block

import com.mojang.serialization.MapCodec
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.block.entity.GymExitEntity
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
//            GuiHandler.openGymLeaveScreen(player)
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}
