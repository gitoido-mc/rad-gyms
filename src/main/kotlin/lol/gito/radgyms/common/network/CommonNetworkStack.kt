/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network

import com.cobblemon.mod.common.net.PacketRegisterInfo
import com.cobblemon.mod.fabric.net.FabricPacketInfo
import lol.gito.radgyms.common.network.client.handler.OpenGymEnterScreenHandler
import lol.gito.radgyms.common.network.client.handler.OpenGymLeaveScreenHandler
import lol.gito.radgyms.common.network.client.payload.GymEnterC2S
import lol.gito.radgyms.common.network.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.network.server.handler.GymEnterC2SHandler
import lol.gito.radgyms.common.network.server.handler.GymLeaveC2SHandler
import lol.gito.radgyms.common.network.server.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.network.server.payload.OpenGymLeaveScreenS2C

object CommonNetworkStack {
    val GYM_ENTER_C2S = FabricPacketInfo(
        PacketRegisterInfo(
            GymEnterC2S.ID,
            GymEnterC2S::decode,
            GymEnterC2SHandler
        )
    )

    val GYM_LEAVE_C2S = FabricPacketInfo(
        PacketRegisterInfo(
            GymLeaveC2S.ID,
            GymLeaveC2S::decode,
            GymLeaveC2SHandler
        )
    )

    val GYM_OPEN_ENTER_SCREEN_S2C = FabricPacketInfo(
        PacketRegisterInfo(
            OpenGymEnterScreenS2C.ID,
            OpenGymEnterScreenS2C::decode,
            OpenGymEnterScreenHandler
        )
    )

    val GYM_OPEN_LEAVE_SCREEN_S2C = FabricPacketInfo(
        PacketRegisterInfo(
            OpenGymLeaveScreenS2C.ID,
            OpenGymLeaveScreenS2C::decode,
            OpenGymLeaveScreenHandler
        )
    )

    fun register() {
        GYM_ENTER_C2S.apply {
            registerPacket(client = false)
            registerServerHandler()
        }

        GYM_LEAVE_C2S.apply {
            registerPacket(client = false)
            registerServerHandler()
        }

        GYM_OPEN_ENTER_SCREEN_S2C.apply {
            registerPacket(client = true)
            registerClientHandler()
        }

        GYM_OPEN_LEAVE_SCREEN_S2C.apply {
            registerPacket(client = true)
            registerClientHandler()
        }
    }
}