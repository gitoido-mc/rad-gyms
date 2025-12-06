/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.data.JsonDataRegistry
import lol.gito.radgyms.common.api.dto.Gym
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import java.io.InputStream
import java.util.concurrent.ExecutionException

object RadGymsTemplates: JsonDataRegistry<Gym.Json> {
    const val SLUG = "gyms"
    override val resourcePath: String = SLUG
    override val id: ResourceLocation = modId(SLUG)
    override val type: PackType = PackType.SERVER_DATA
    override val observable: SimpleObservable<RadGymsTemplates> = SimpleObservable<RadGymsTemplates>()

    val templates = mutableMapOf<String, Gym.Json>()

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(ExecutionException::class)
    override fun parse(
        stream: InputStream,
        identifier: ResourceLocation
    ): Gym.Json {
        return try {
            debug("Parsing $identifier...")
            Json.decodeFromStream<Gym.Json>(stream)
        } catch (exception: Exception) {
            throw ExecutionException("Error parsing JSON from resource $identifier", exception)
        }
    }

    override fun reload(data: Map<ResourceLocation, Gym.Json>) {
        templates.clear()
        data.forEach { (key, value) ->
            debug("adding $key template")
            templates[key.path] = value
        }
        observable.emit(this)
    }

    override fun sync(player: ServerPlayer) {}
}