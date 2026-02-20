/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.net

import com.cobblemon.mod.common.NetworkManager
import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.fabric.net.FabricPacketInfo
import lol.gito.radgyms.common.net.RadGymsNetwork
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer

object RadGymsFabricNetworkManager : NetworkManager {
    fun registerMessages() {
        RadGymsNetwork.s2cPayloads.map { FabricPacketInfo(it) }.forEach { it.registerPacket(client = true) }
        RadGymsNetwork.c2sPayloads.map { FabricPacketInfo(it) }.forEach { it.registerPacket(client = false) }
    }

    fun registerClientHandlers() =
        RadGymsNetwork.s2cPayloads.map { FabricPacketInfo(it) }.forEach { it.registerClientHandler() }

    fun registerServerHandlers() =
        RadGymsNetwork.c2sPayloads.map { FabricPacketInfo(it) }.forEach { it.registerServerHandler() }

    override fun sendPacketToPlayer(player: ServerPlayer, packet: NetworkPacket<*>) =
        ServerPlayNetworking.send(player, packet)

    override fun sendToServer(packet: NetworkPacket<*>) = ClientPlayNetworking.send(packet)
}
