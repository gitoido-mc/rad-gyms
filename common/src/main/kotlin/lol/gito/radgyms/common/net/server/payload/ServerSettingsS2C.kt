/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.net.server.payload

import com.cobblemon.mod.common.api.net.NetworkPacket
import lol.gito.radgyms.common.RadGyms.modId
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation

class ServerSettingsS2C(
    val maxEntranceUses: Int,
    val shardRewards: Boolean,
    val lapisBoostAmount: Int,
    val ignoredSpecies: List<String>,
    val ignoredForms: List<String>,
    val minLevel: Int,
    val maxLevel: Int
) :
    NetworkPacket<ServerSettingsS2C> {
    override val id: ResourceLocation = ID

    companion object {
        val ID = modId("net.server_settings")

        fun decode(buffer: RegistryFriendlyByteBuf) = ServerSettingsS2C(
            ByteBufCodecs.INT.decode(buffer),
            ByteBufCodecs.BOOL.decode(buffer),
            ByteBufCodecs.INT.decode(buffer),
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).decode(buffer),
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).decode(buffer),
            ByteBufCodecs.INT.decode(buffer),
            ByteBufCodecs.INT.decode(buffer),
        )
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        ByteBufCodecs.INT.encode(buffer, maxEntranceUses)
        ByteBufCodecs.BOOL.encode(buffer, shardRewards)
        ByteBufCodecs.INT.encode(buffer, lapisBoostAmount)
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).encode(buffer, ignoredSpecies)
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).encode(buffer, ignoredForms)
        ByteBufCodecs.INT.encode(buffer, minLevel)
        ByteBufCodecs.INT.encode(buffer, maxLevel)
    }
}