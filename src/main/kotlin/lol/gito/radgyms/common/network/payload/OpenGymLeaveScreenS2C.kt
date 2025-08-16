/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.payload

import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

class OpenGymLeaveScreenS2C(val id: Identifier) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_leave_screen")
        val ID = CustomPayload.Id<OpenGymLeaveScreenS2C>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, OpenGymLeaveScreenS2C> =
            PacketCodec.of<RegistryByteBuf, OpenGymLeaveScreenS2C>(
                { value, buffer ->
                    PacketCodecs.STRING.encode(buffer, value.id.toString())
                },
                { buffer ->
                    OpenGymLeaveScreenS2C(
                        Identifier.of(PacketCodecs.STRING.decode(buffer))
                    )
                }
            )
    }
}