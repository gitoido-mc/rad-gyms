/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.network.payload

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.pokecache.CacheDTO
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

@JvmRecord
data class CachePokeSync(
    val id: Identifier = modId("net.cache_poke_sync"),
    val data: MutableMap<String, CacheDTO> = SpeciesManager.SPECIES_BY_RARITY
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload> = CustomPayload.Id(this.id)
}