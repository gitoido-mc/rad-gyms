/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.payload

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos
import java.util.*
import kotlin.jvm.optionals.getOrNull

class OpenGymEnterScreenS2C(
    val derivedLevel: Int,
    val key: Boolean,
    val type: String,
    val pos: BlockPos? = null
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = ID

    companion object {
        val PACKET_ID = modId("net.gym_enter_screen")
        val ID = CustomPayload.Id<OpenGymEnterScreenS2C>(PACKET_ID)
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, OpenGymEnterScreenS2C> =
            PacketCodec.of<RegistryByteBuf, OpenGymEnterScreenS2C>(
                { value, buffer ->
                    debug("Derived level is ${value.derivedLevel}")
                    PacketCodecs.INTEGER.encode(buffer, value.derivedLevel)
                    PacketCodecs.BOOL.encode(buffer, value.key)
                    PacketCodecs.STRING.encode(buffer, value.type)
                    PacketCodecs.optional(BlockPos.PACKET_CODEC).encode(buffer, Optional.ofNullable(value.pos))

                },
                { buffer ->
                    OpenGymEnterScreenS2C(
                        PacketCodecs.INTEGER.decode(buffer),
                        PacketCodecs.BOOL.decode(buffer),
                        PacketCodecs.STRING.decode(buffer),
                        PacketCodecs.optional(BlockPos.PACKET_CODEC).decode(buffer).getOrNull()
                    )
                }
            )
    }
}