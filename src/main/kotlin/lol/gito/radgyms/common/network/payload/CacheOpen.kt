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
import net.minecraft.util.Rarity

class CacheOpen(
    val type: String,
    val rarity: Rarity,
    val shinyBoost: Int,
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.cache_open")
        val ID = CustomPayload.Id<CacheOpen>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, CacheOpen> = PacketCodec.of<RegistryByteBuf, CacheOpen>(
            { value, buffer ->
                PacketCodecs.STRING.encode(buffer, value.type)
                Rarity.PACKET_CODEC.encode(buffer, value.rarity)
                PacketCodecs.INTEGER.encode(buffer, value.shinyBoost)
            },
            { buffer ->
                CacheOpen(
                    PacketCodecs.STRING.decode(buffer),
                    Rarity.PACKET_CODEC.decode(buffer),
                    PacketCodecs.INTEGER.decode(buffer)
                )
            }
        )
    }
}