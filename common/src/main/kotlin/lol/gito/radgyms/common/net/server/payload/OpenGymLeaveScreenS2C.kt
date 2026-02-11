/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.server.payload

import com.cobblemon.mod.common.api.net.NetworkPacket
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation

class OpenGymLeaveScreenS2C : NetworkPacket<OpenGymLeaveScreenS2C> {
    override val id: ResourceLocation = ID

    companion object {
        val ID = modId("net.gym_leave_screen")

        @Suppress("unused_parameter")
        fun decode(ignored: RegistryFriendlyByteBuf) = OpenGymLeaveScreenS2C()
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) = Unit
}
