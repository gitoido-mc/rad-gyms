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
import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.registry.ItemRegistry.EXIT_ROPE
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class GymExitBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun getCodec(): MapCodec<out BlockWithEntity> =
        createCodec { settings: Settings -> GymExitBlock(settings) }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = GymExitEntity(pos, state)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.PASS
        if (world.getBlockEntity(pos) !is GymExitEntity) return super.onUse(state, world, pos, player, hit)

        debug("Gym exit block used by player ${player.uuid} at $pos in ${world.registryKey}")

        if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            debug("Client: Opening gym exit screen for ${player.uuid} at $pos in ${world.registryKey}")
            ServerPlayNetworking.send(
                player as ServerPlayerEntity, OpenGymLeaveScreenS2C(OpenGymLeaveScreenS2C.PACKET_ID)
            )
        } else {
            player.sendMessage(translatable(EXIT_ROPE.translationKey.plus(".failed")))
        }

        return ActionResult.SUCCESS
    }
}
