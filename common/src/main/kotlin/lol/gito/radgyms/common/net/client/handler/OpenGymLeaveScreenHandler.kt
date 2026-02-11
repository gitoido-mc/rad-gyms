/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.client.handler

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import lol.gito.radgyms.common.client.render.gui.screen.GymLeaveScreen
import lol.gito.radgyms.common.net.server.payload.OpenGymLeaveScreenS2C
import net.minecraft.client.Minecraft

/**
 * Emits open gym entrance screen event in mod bus
 */
object OpenGymLeaveScreenHandler : ClientNetworkPacketHandler<OpenGymLeaveScreenS2C> {
    override fun handle(
        packet: OpenGymLeaveScreenS2C,
        client: Minecraft
    ) {
        client.setScreen(GymLeaveScreen())
    }
}
