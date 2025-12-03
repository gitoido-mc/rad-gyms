/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.net.server.payload

import com.cobblemon.mod.common.api.net.NetworkPacket
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

/**
 * Tells client to open gym entrance screen
 */
class OpenGymEnterScreenS2C(
    val derivedLevel: Int,
    val key: Boolean,
    val type: String,
    val pos: BlockPos? = null,
    val usesLeft: Int? = null
) : NetworkPacket<OpenGymEnterScreenS2C> {
    override val id: ResourceLocation = ID

    companion object {
        val ID = modId("net.gym_enter_screen")

        fun decode(buffer: RegistryFriendlyByteBuf) = OpenGymEnterScreenS2C(
            ByteBufCodecs.INT.decode(buffer),
            ByteBufCodecs.BOOL.decode(buffer),
            ByteBufCodecs.STRING_UTF8.decode(buffer),
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC).decode(buffer).getOrNull(),
            ByteBufCodecs.optional(ByteBufCodecs.INT).decode(buffer).getOrNull()
        )
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        ByteBufCodecs.INT.encode(buffer, derivedLevel)
        ByteBufCodecs.BOOL.encode(buffer, key)
        ByteBufCodecs.STRING_UTF8.encode(buffer, type)
        ByteBufCodecs.optional(BlockPos.STREAM_CODEC).encode(buffer, Optional.ofNullable(pos))
        ByteBufCodecs.optional(ByteBufCodecs.INT).encode(buffer, Optional.ofNullable(usesLeft))
    }
}