/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.net

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.net.PacketRegisterInfo
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.net.client.handler.OpenGymEnterScreenHandler
import lol.gito.radgyms.common.net.client.handler.OpenGymLeaveScreenHandler
import lol.gito.radgyms.common.net.client.payload.GymEnterC2S
import lol.gito.radgyms.common.net.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.net.server.handler.GymEnterC2SHandler
import lol.gito.radgyms.common.net.server.handler.GymLeaveC2SHandler
import lol.gito.radgyms.common.net.server.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.net.server.payload.OpenGymLeaveScreenS2C
import net.minecraft.server.level.ServerPlayer

object RadGymsNetwork {
    fun ServerPlayer.sendPacket(packet: NetworkPacket<*>) {
        sendPacketToPlayer(this, packet)
    }

    @JvmStatic
    fun sendToServer(packet: NetworkPacket<*>) {
        RadGyms.implementation.networkManager.sendToServer(packet)
    }

    @JvmStatic
    fun sendToAllPlayers(packet: NetworkPacket<*>) =
        sendPacketToPlayers(RadGyms.implementation.server()!!.playerList.players, packet)

    @JvmStatic
    fun sendPacketToPlayers(players: Iterable<ServerPlayer>, packet: NetworkPacket<*>) =
        players.forEach { sendPacketToPlayer(it, packet) }

    val s2cPayloads = generateS2CPacketInfoList()
    val c2sPayloads = generateC2SPacketInfoList()


    fun sendPacketToPlayer(player: ServerPlayer, packet: NetworkPacket<*>) {
        RadGyms.implementation.networkManager.sendPacketToPlayer(player, packet)
    }

    private fun generateS2CPacketInfoList(): List<PacketRegisterInfo<*>> = listOf(
        PacketRegisterInfo(
            OpenGymEnterScreenS2C.ID,
            OpenGymEnterScreenS2C::decode,
            OpenGymEnterScreenHandler
        ),
        PacketRegisterInfo(
            OpenGymLeaveScreenS2C.ID,
            OpenGymLeaveScreenS2C::decode,
            OpenGymLeaveScreenHandler
        )
    )

    private fun generateC2SPacketInfoList(): List<PacketRegisterInfo<*>> = listOf(
        PacketRegisterInfo(
            GymEnterC2S.ID,
            GymEnterC2S::decode,
            GymEnterC2SHandler
        ),
        PacketRegisterInfo(
            GymLeaveC2S.ID,
            GymLeaveC2S::decode,
            GymLeaveC2SHandler
        )
    )
}