/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.client.payload

import com.cobblemon.mod.common.api.net.NetworkPacket
import lol.gito.radgyms.common.RadGyms
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation

class GymLeaveC2S(val teleport: Boolean) : NetworkPacket<GymLeaveC2S> {
    override val id: ResourceLocation = ID

    companion object {
        val ID = RadGyms.modId("net.gym_leave")

        fun decode(buffer: RegistryFriendlyByteBuf) = GymLeaveC2S(ByteBufCodecs.BOOL.decode(buffer))
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        ByteBufCodecs.BOOL.encode(buffer, teleport)
    }
}