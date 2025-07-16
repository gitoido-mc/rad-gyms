/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos

@Environment(EnvType.CLIENT)
object GuiHandler {
    @Environment(EnvType.CLIENT)
    fun openGymKeyScreen(player: PlayerEntity, type: String? = null) =
        MinecraftClient
            .getInstance()
            .execute {
                MinecraftClient.getInstance().setScreen(
                    GymEnterScreen(
                        type = type,
                        player = player
                    )
                )
            }

    @Environment(EnvType.CLIENT)
    fun openGymEntranceScreen(player: PlayerEntity, type: String, blockPos: BlockPos, usesLeft: Int) =
        MinecraftClient
            .getInstance()
            .execute {
                MinecraftClient.getInstance().setScreen(
                    GymEntranceScreen(
                        type = type,
                        player = player,
                        blockPos = blockPos,
                        usesLeft = usesLeft
                    )
                )
            }


    @Environment(EnvType.CLIENT)
    fun openGymLeaveScreen(player: PlayerEntity) =
        MinecraftClient
            .getInstance()
            .execute {
                MinecraftClient.getInstance().setScreen(GymLeaveScreen(player))
            }

    @Environment(EnvType.CLIENT)
    fun openCacheAttuneScreen(player: PlayerEntity, rarity: Rarity, shinyBoost: Int) =
        MinecraftClient
            .getInstance()
            .execute {
                MinecraftClient.getInstance().setScreen(CacheAttuneScreen(player, rarity, shinyBoost))
            }
}
