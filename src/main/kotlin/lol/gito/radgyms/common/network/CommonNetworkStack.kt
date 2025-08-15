/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import lol.gito.radgyms.common.network.handler.CacheOpenPacketHandler
import lol.gito.radgyms.common.network.handler.GymEnterPacketHandler
import lol.gito.radgyms.common.network.handler.GymLeavePacketHandler
import lol.gito.radgyms.common.network.payload.CacheOpen
import lol.gito.radgyms.common.network.payload.GymEnter
import lol.gito.radgyms.common.network.payload.GymLeave
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object CommonNetworkStack {
    fun register() {
        PayloadTypeRegistry.playC2S().register(
            GymEnter.ID,
            GymEnter.PACKET_CODEC
        )
        PayloadTypeRegistry.playC2S().register(
            GymLeave.ID,
            GymLeave.PACKET_CODEC
        )
        PayloadTypeRegistry.playC2S().register(
            CacheOpen.ID,
            CacheOpen.PACKET_CODEC
        )

        ServerPlayNetworking.registerGlobalReceiver(GymEnter.ID, ::GymEnterPacketHandler)
        ServerPlayNetworking.registerGlobalReceiver(GymLeave.ID, ::GymLeavePacketHandler)
        ServerPlayNetworking.registerGlobalReceiver(CacheOpen.ID, ::CacheOpenPacketHandler)
    }
}