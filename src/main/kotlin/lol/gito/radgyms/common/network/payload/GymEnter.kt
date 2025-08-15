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

class GymEnter(
    val key: Boolean,
    val level: Int,
    val type: String? = null
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_enter")
        val ID = CustomPayload.Id<GymEnter>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GymEnter> = PacketCodec.of<RegistryByteBuf, GymEnter>(
            { value, buffer ->
                PacketCodecs.BOOL.encode(buffer, value.key)
                PacketCodecs.INTEGER.encode(buffer, value.level)
                PacketCodecs.STRING.apply(PacketCodecs::optional).encode(buffer, value.type)
            },
            { buffer ->
                GymEnter(
                    PacketCodecs.BOOL.decode(buffer),
                    PacketCodecs.INTEGER.decode(buffer),
                    PacketCodecs.STRING.apply(PacketCodecs::optional).decode(buffer)
                )
            }
        )
    }
}