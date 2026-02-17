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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.data.JsonDataRegistry
import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.api.dto.reward.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import java.io.InputStream
import java.util.concurrent.ExecutionException

object RadGymsTemplates : JsonDataRegistry<GymJson> {
    const val SLUG = "gyms"
    override val resourcePath: String = SLUG
    override val id: ResourceLocation = modId(SLUG)
    override val type: PackType = PackType.SERVER_DATA
    override val observable: SimpleObservable<RadGymsTemplates> = SimpleObservable<RadGymsTemplates>()

    val templates = mutableMapOf<String, GymJson>()

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(ExecutionException::class)
    override fun parse(
        stream: InputStream,
        identifier: ResourceLocation
    ): GymJson {
        val module = SerializersModule {
            polymorphic(RewardInterface::class) {
                subclass(CommandReward::class)
                subclass(LootTableReward::class)
                subclass(PokemonReward::class)
                subclass(AdvancementReward::class)
            }
        }

        val format = Json {
            serializersModule = module
        }

        return format.decodeFromStream<GymJson>(stream)
    }

    override fun reload(data: Map<ResourceLocation, GymJson>) {
        templates.clear()
        data.forEach { (key, value) ->
            templates[key.path] = value
        }
        info("Loaded ${templates.count()} gym templates")
        observable.emit(this)
    }

    override fun sync(player: ServerPlayer) = Unit
}
