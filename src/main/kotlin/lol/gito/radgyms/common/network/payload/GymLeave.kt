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

class GymLeave(
    val teleport: Boolean
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_leave")
        val ID = CustomPayload.Id<GymLeave>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GymLeave> = PacketCodec.of<RegistryByteBuf, GymLeave>(
            { value, buffer ->
                PacketCodecs.BOOL.encode(buffer, value.teleport)
            },
            { buffer ->
                GymLeave(PacketCodecs.BOOL.decode(buffer))
            }
        )
    }
}