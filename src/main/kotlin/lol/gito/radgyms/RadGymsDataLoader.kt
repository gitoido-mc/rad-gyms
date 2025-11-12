/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms

import com.cobblemon.mod.common.util.endsWith
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.common.gym.GymDTO
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.pokecache.CacheDTO
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import java.io.File

class RadGymsDataLoader {
    companion object GymsResourceReloadListener : SimpleSynchronousResourceReloadListener {
        override fun getFabricId(): Identifier {
            return modId("gyms")
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun reload(manager: ResourceManager) {
            GymManager.GYM_TEMPLATES.clear()
            SpeciesManager.SPECIES_BY_RARITY = mutableMapOf()

            manager.findResources("gyms") { path -> path.endsWith(".json") }
                .forEach { (id: Identifier, res: Resource) ->
                    try {
                        val templateName = File(id.path).nameWithoutExtension
                        GymManager.GYM_TEMPLATES[templateName] = Json.Default.decodeFromStream<GymDTO>(res.inputStream)
                        debug("Loaded $templateName template from ${File(id.path).name} gym config")
                    } catch (e: Exception) {
                        RadGyms.LOGGER.warn("Could not parse ${File(id.path).name} gym data", e)
                    }
                }

            manager.findResources("caches") { path -> path.endsWith(".json") }
                .forEach { (id: Identifier, res: Resource) ->
                    try {
                        val cacheConfig = File(id.path).nameWithoutExtension
                        SpeciesManager.SPECIES_BY_RARITY[cacheConfig] =
                            Json.Default.decodeFromStream<CacheDTO>(res.inputStream)
                    } catch (e: Exception) {
                        RadGyms.LOGGER.warn("Could not parse ${File(id.path).name} cache data", e)
                    }
                }
        }
    }

    fun register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GymsResourceReloadListener)
    }
}