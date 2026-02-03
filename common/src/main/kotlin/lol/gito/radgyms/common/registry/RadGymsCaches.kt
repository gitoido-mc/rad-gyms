/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.data.JsonDataRegistry
import lol.gito.radgyms.common.pokecache.CacheDTO
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import java.io.InputStream
import java.util.concurrent.ExecutionException

object RadGymsCaches : JsonDataRegistry<CacheDTO> {
    const val SLUG = "caches"
    override val resourcePath: String = SLUG
    override val id: ResourceLocation = modId(SLUG)
    override val type: PackType = PackType.SERVER_DATA
    override val observable: SimpleObservable<RadGymsCaches> = SimpleObservable<RadGymsCaches>()

    val caches = mutableMapOf<ResourceLocation, CacheDTO>()

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(ExecutionException::class)
    override fun parse(
        stream: InputStream,
        identifier: ResourceLocation
    ): CacheDTO = Json.decodeFromStream<CacheDTO>(stream)

    override fun reload(data: Map<ResourceLocation, CacheDTO>) {
        caches.clear()
        caches.putAll(data)
        info("Loaded ${caches.count()} cache configurations")
        observable.emit(this)
    }

    override fun sync(player: ServerPlayer) {}
}