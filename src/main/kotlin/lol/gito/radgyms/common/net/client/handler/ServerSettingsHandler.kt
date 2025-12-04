/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.client.handler

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.config.RadGymsConfig
import lol.gito.radgyms.common.net.server.payload.ServerSettingsS2C
import net.minecraft.client.Minecraft

/**
 * Emits open gym entrance screen event in mod bus
 */
object ServerSettingsHandler : ClientNetworkPacketHandler<ServerSettingsS2C> {
    override fun handle(packet: ServerSettingsS2C, client: Minecraft) {
        val serverConfig = RadGymsConfig(
            maxEntranceUses = packet.maxEntranceUses,
            shardRewards = packet.shardRewards,
            lapisBoostAmount = packet.lapisBoostAmount,
            ignoredSpecies = packet.ignoredSpecies,
            ignoredForms = packet.ignoredForms,
            minLevel = packet.minLevel,
            maxLevel = packet.maxLevel
        )

        CONFIG = CONFIG.combine(serverConfig)
        debug("Received server config for Rad Gyms")
    }
}