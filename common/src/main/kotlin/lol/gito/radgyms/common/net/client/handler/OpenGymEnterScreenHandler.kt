/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.client.handler

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.net.server.payload.OpenGymEnterScreenS2C
import net.minecraft.client.Minecraft

/**
 * Emits open gym entrance screen event in mod bus
 */
object OpenGymEnterScreenHandler : ClientNetworkPacketHandler<OpenGymEnterScreenS2C> {
    override fun handle(packet: OpenGymEnterScreenS2C, client: Minecraft) = GymEvents.ENTER_SCREEN_OPEN.emit(
        GymEvents.GymEnterScreenOpenEvent(
            packet.pos,
            packet.key,
            packet.type,
            RadGyms.config.minLevel!!,
            RadGyms.config.maxLevel!!,
            packet.derivedLevel,
            packet.usesLeft,
        ),
    )
}
