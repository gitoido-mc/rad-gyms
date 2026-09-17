/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import com.cobblemon.mod.common.util.adapters.IdentifierAdapter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.data.ServerJsonDataRegistry
import lol.gito.radgyms.common.cache.CacheDTO
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType

object RadGymsCaches : ServerJsonDataRegistry<CacheDTO> {
    const val SLUG = "caches"
    override val resourcePath: String = SLUG
    override val id: ResourceLocation = modId(SLUG)
    override val type: PackType = PackType.SERVER_DATA
    override val observable: SimpleObservable<RadGymsCaches> = SimpleObservable<RadGymsCaches>()
    override val typeToken: TypeToken<CacheDTO> = TypeToken.of(CacheDTO::class.java)
    val caches = mutableMapOf<ResourceLocation, CacheDTO>()

    override fun setupParser(registry: HolderLookup.Provider): Gson = GsonBuilder()
        .registerTypeAdapter(ResourceLocation::class.java, IdentifierAdapter)
        .setPrettyPrinting()
        .create()

    override fun reload(data: Map<ResourceLocation, CacheDTO>, registry: HolderLookup.Provider) {
        caches.clear()
        caches.putAll(data)
        info("Loaded ${caches.count()} cache configurations")
        observable.emit(this)
    }

    /**
     * We dont sync caches on client
     *
     * @param player
     */
    override fun sync(player: ServerPlayer) = Unit
}
