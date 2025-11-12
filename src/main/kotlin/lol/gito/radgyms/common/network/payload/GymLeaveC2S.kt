/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.payload

import lol.gito.radgyms.RadGyms.modId
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class GymLeaveC2S(
    val teleport: Boolean
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_leave")
        val ID = CustomPayload.Id<GymLeaveC2S>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GymLeaveC2S> = PacketCodec.of<RegistryByteBuf, GymLeaveC2S>(
            { value, buffer ->
                PacketCodecs.BOOL.encode(buffer, value.teleport)
            },
            { buffer ->
                GymLeaveC2S(PacketCodecs.BOOL.decode(buffer))
            }
        )
    }
}