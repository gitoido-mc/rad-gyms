/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym

import lol.gito.radgyms.common.util.delayExecute
import lol.gito.radgyms.common.util.displayClientMessage
import lol.gito.radgyms.common.world.PlayerSpawnHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class GymTeleportScheduler {
    fun scheduleReturnWithCountdown(player: ServerPlayer, dim: ServerLevel, pos: BlockPos) {
        player.displayClientMessage(Component.nullToEmpty("5...") as MutableComponent)
        delayExecute(1f) {
            player.displayClientMessage(Component.nullToEmpty("4...") as MutableComponent)
        }
        delayExecute(2f) {
            player.displayClientMessage(Component.nullToEmpty("3...") as MutableComponent)
        }
        delayExecute(3f) {
            player.displayClientMessage(Component.nullToEmpty("2...") as MutableComponent)
        }
        delayExecute(4f) {
            player.displayClientMessage(Component.nullToEmpty("1...") as MutableComponent)
        }
        delayExecute(5f) {
            PlayerSpawnHelper.teleportPlayer(player.uuid, dim, pos, yaw = player.yRot, pitch = player.xRot)
        }
    }
}