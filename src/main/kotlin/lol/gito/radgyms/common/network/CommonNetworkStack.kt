/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import lol.gito.radgyms.common.network.handler.CacheOpenC2SHandler
import lol.gito.radgyms.common.network.handler.GymEnterC2SHandler
import lol.gito.radgyms.common.network.handler.GymLeaveC2SHandler
import lol.gito.radgyms.common.network.payload.*
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object CommonNetworkStack {
    fun register() {
        PayloadTypeRegistry.playC2S().register(
            GymEnterC2S.ID,
            GymEnterC2S.PACKET_CODEC
        )
        PayloadTypeRegistry.playC2S().register(
            GymLeaveC2S.ID,
            GymLeaveC2S.PACKET_CODEC
        )
        PayloadTypeRegistry.playC2S().register(
            CacheOpenC2S.ID,
            CacheOpenC2S.PACKET_CODEC
        )
        PayloadTypeRegistry.playS2C().register(
            OpenGymEnterScreenS2C.ID,
            OpenGymEnterScreenS2C.PACKET_CODEC
        )
        PayloadTypeRegistry.playS2C().register(
            OpenGymLeaveScreenS2C.ID,
            OpenGymLeaveScreenS2C.PACKET_CODEC
        )

        ServerPlayNetworking.registerGlobalReceiver(GymEnterC2S.ID, ::GymEnterC2SHandler)
        ServerPlayNetworking.registerGlobalReceiver(GymLeaveC2S.ID, ::GymLeaveC2SHandler)
        ServerPlayNetworking.registerGlobalReceiver(CacheOpenC2S.ID, ::CacheOpenC2SHandler)
    }
}