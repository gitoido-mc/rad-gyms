/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import com.google.common.collect.HashBiMap
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.reward.AdvancementReward
import lol.gito.radgyms.common.api.dto.reward.CommandReward
import lol.gito.radgyms.common.api.dto.reward.LootTableReward
import lol.gito.radgyms.common.api.dto.reward.PokemonReward
import lol.gito.radgyms.common.api.dto.reward.RewardInterface
import java.lang.reflect.Type
import kotlin.reflect.KClass

object RewardTypeAdapter : JsonDeserializer<RewardInterface>, JsonSerializer<RewardInterface> {
    private val types = HashBiMap.create<String, KClass<out RewardInterface>>()

    init {
        registerType(modId(AdvancementReward.ID).toString(), AdvancementReward::class)
        registerType(modId(CommandReward.ID).toString(), CommandReward::class)
        registerType(modId(LootTableReward.ID).toString(), LootTableReward::class)
        registerType(modId(PokemonReward.ID).toString(), PokemonReward::class)
    }

    fun <T : RewardInterface> registerType(id: String, type: KClass<T>) {
        this.types[id.lowercase()] = type
    }

    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): RewardInterface {
        val type = json.asJsonObject.get("type").asString.lowercase()
        val reward = this.types[type] ?: error("Cannot resolve reward variant for type $type")
        return context.deserialize(json, reward.java)
    }

    override fun serialize(reward: RewardInterface, type: Type, context: JsonSerializationContext): JsonElement {
        val json = context.serialize(reward, reward::class.java).asJsonObject
        val variant = types.inverse()[reward::class] ?: error("Cannot resolve reward variant for type $type")
        json.addProperty("type", variant)
        return json
    }
}
