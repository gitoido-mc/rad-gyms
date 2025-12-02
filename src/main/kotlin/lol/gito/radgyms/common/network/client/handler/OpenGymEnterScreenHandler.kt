/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.client.handler

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.network.server.payload.OpenGymEnterScreenS2C
import net.minecraft.client.Minecraft

/**
 * Emits open gym entrance screen event in mod bus
 */
object OpenGymEnterScreenHandler : ClientNetworkPacketHandler<OpenGymEnterScreenS2C> {
    override fun handle(
        packet: OpenGymEnterScreenS2C,
        client: Minecraft
    ) {
        // Config or someone might be able to override some stuff via event
        GymEvents.ENTER_SCREEN_OPEN.emit(
            GymEvents.GymEnterScreenOpenEvent(
                packet.pos,
                packet.key,
                packet.type,
                RadGyms.CONFIG.minLevel!!,
                RadGyms.CONFIG.maxLevel!!,
                packet.derivedLevel,
                packet.usesLeft
            )
        )
    }
}