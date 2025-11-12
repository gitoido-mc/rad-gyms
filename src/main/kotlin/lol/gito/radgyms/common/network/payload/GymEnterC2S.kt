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
import net.minecraft.util.math.BlockPos
import java.util.*
import kotlin.jvm.optionals.getOrNull

class GymEnterC2S(
    val key: Boolean,
    val level: Int,
    val type: String? = null,
    val pos: BlockPos? = null
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_enter")
        val ID = CustomPayload.Id<GymEnterC2S>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, GymEnterC2S> = PacketCodec.of<RegistryByteBuf, GymEnterC2S>(
            { value, buffer ->
                PacketCodecs.BOOL.encode(buffer, value.key)
                PacketCodecs.INTEGER.encode(buffer, value.level)
                PacketCodecs.STRING.apply(PacketCodecs::optional).encode(buffer, value.type)
                PacketCodecs.optional(BlockPos.PACKET_CODEC).encode(buffer, Optional.ofNullable(value.pos))
            },
            { buffer ->
                GymEnterC2S(
                    PacketCodecs.BOOL.decode(buffer),
                    PacketCodecs.INTEGER.decode(buffer),
                    PacketCodecs.STRING.apply(PacketCodecs::optional).decode(buffer),
                    PacketCodecs.optional(BlockPos.PACKET_CODEC).decode(buffer).getOrNull()
                )
            }
        )
    }
}