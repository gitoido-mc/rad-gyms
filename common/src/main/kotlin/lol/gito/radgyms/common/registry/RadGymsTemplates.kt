/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.api.reactive.SimpleObservable
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.adapters.ElementalTypeAdapter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import lol.gito.radgyms.common.RadGyms.info
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.data.JsonDataRegistry
import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import lol.gito.radgyms.common.api.serialization.RewardTypeAdapter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.packs.PackType
import net.minecraft.util.LowerCaseEnumTypeAdapterFactory

object RadGymsTemplates : JsonDataRegistry<GymJson> {
    const val SLUG = "gyms"
    override val resourcePath: String = SLUG
    override val id: ResourceLocation = modId(SLUG)
    override val type: PackType = PackType.SERVER_DATA
    override val observable: SimpleObservable<RadGymsTemplates> = SimpleObservable<RadGymsTemplates>()
    override val typeToken: TypeToken<GymJson> = TypeToken.of(GymJson::class.java)
    override val gson: Gson = GsonBuilder()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .registerTypeAdapter(RewardInterface::class.java, RewardTypeAdapter)
        .registerTypeAdapter(ElementalType::class.java, ElementalTypeAdapter)
        .registerTypeAdapterFactory(LowerCaseEnumTypeAdapterFactory())
        .create()

    val templates = mutableMapOf<String, GymJson>()

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
