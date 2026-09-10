/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.scheduling.afterOnServer
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class GymTeleportScheduler {
    @Suppress("MagicNumber")
    fun scheduleReturnWithCountdown(
        player: ServerPlayer,
        dim: ServerLevel,
        pos: BlockPos,
    ) {
        player.displayClientMessage(Component.nullToEmpty("5..."))
        afterOnServer(1f) {
            player.displayClientMessage(Component.nullToEmpty("4..."))
        }
        afterOnServer(2f) {
            player.displayClientMessage(Component.nullToEmpty("3..."))
        }
        afterOnServer(3f) {
            player.displayClientMessage(Component.nullToEmpty("2..."))
        }
        afterOnServer(4f) {
            player.displayClientMessage(Component.nullToEmpty("1..."))
        }
        afterOnServer(5f) {
            PlayerSpawnHelper.teleportPlayer(player.uuid, dim, pos, yaw = player.yRot, pitch = player.xRot)
        }
    }
}
