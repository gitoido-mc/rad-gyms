/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.client.payload

import com.cobblemon.mod.common.api.net.NetworkPacket
import lol.gito.radgyms.common.RadGyms
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

class GymEnterC2S(
    val key: Boolean,
    val level: Int,
    val type: String? = null,
    val pos: BlockPos? = null
) : NetworkPacket<GymEnterC2S> {
    override val id: ResourceLocation = ID

    companion object {
        val ID = RadGyms.modId("net.gym_enter")

        fun decode(buffer: RegistryFriendlyByteBuf) = GymEnterC2S(
            ByteBufCodecs.BOOL.decode(buffer),
            ByteBufCodecs.INT.decode(buffer),
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8).decode(buffer).getOrNull(),
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC).decode(buffer).getOrNull()
        )
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        ByteBufCodecs.BOOL.encode(buffer, key)
        ByteBufCodecs.INT.encode(buffer, level)
        ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8).encode(buffer, Optional.ofNullable(type))
        ByteBufCodecs.optional(BlockPos.STREAM_CODEC).encode(buffer, Optional.ofNullable(pos))
    }
}
