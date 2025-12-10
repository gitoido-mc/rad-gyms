/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.neoforge.net

import com.cobblemon.mod.common.NetworkManager
import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.client.net.data.DataRegistrySyncPacketHandler
import com.cobblemon.mod.neoforge.net.NeoForgePacketInfo
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.net.RadGymsNetwork
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.HandlerThread

object RadGymsNeoForgeNetworkManager : NetworkManager {
    const val PROTOCOL_VERSION = "0.4.0"

    fun registerMessages(event: RegisterPayloadHandlersEvent) {
        val registrar = event
            .registrar(RadGyms.MOD_ID)
            .versioned(PROTOCOL_VERSION)

        val netRegistrar = event
            .registrar(RadGyms.MOD_ID)
            .versioned(PROTOCOL_VERSION)
            .executesOn(HandlerThread.NETWORK)

        val syncPackets = HashSet<ResourceLocation>()
        val asyncPackets = HashSet<ResourceLocation>()

        RadGymsNetwork.s2cPayloads.map { NeoForgePacketInfo(it) }.forEach {
            val handleAsync = it.info.handler is DataRegistrySyncPacketHandler<*, *>
            if (handleAsync) asyncPackets += it.info.id
            else syncPackets += it.info.id

            it.registerToClient(if (handleAsync) netRegistrar else registrar)
        }

        RadGymsNetwork.c2sPayloads.map { NeoForgePacketInfo(it) }.forEach {
            it.registerToServer(registrar)
        }
    }

    override fun sendPacketToPlayer(
        player: ServerPlayer,
        packet: NetworkPacket<*>
    ) = player.connection.send(packet)

    override fun sendToServer(packet: NetworkPacket<*>) {
        Minecraft.getInstance().connection?.send(packet)
    }
}