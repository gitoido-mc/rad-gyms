/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric

import com.cobblemon.mod.common.util.endsWith
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.pokecache.CacheDTO
import lol.gito.radgyms.common.registry.RadGymsTemplates
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import java.io.File

class RadGymsDataLoader {
    companion object GymsResourceReloadListener : SimpleSynchronousResourceReloadListener {
        override fun getFabricId(): ResourceLocation {
            return RadGyms.modId("gyms")
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun onResourceManagerReload(manager: ResourceManager) {
            RadGymsTemplates.clearTemplates()
            SpeciesManager.SPECIES_BY_RARITY = mutableMapOf()

            manager.listResources("gyms") { path -> path.endsWith(".json") }
                .forEach { (id: ResourceLocation, res: Resource) ->
                    try {
                        val templateName = File(id.path).nameWithoutExtension
                        RadGymsTemplates.registerTemplate(
                            templateName,
                            Json.decodeFromStream<Gym.Json>(res.open())
                        )
                        RadGyms.debug("Loaded $templateName template from ${File(id.path).name} gym config")
                    } catch (e: Exception) {
                        RadGyms.LOGGER.warn("Could not parse ${File(id.path).name} gym data", e)
                    }
                }

            manager.listResources("caches") { path -> path.endsWith(".json") }
                .forEach { (id: ResourceLocation, res: Resource) ->
                    try {
                        val cacheConfig = File(id.path).nameWithoutExtension
                        SpeciesManager.SPECIES_BY_RARITY[cacheConfig] =
                            Json.decodeFromStream<CacheDTO>(res.open())
                    } catch (e: Exception) {
                        RadGyms.LOGGER.warn("Could not parse ${File(id.path).name} cache data", e)
                    }
                }
        }
    }

    fun register() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(GymsResourceReloadListener)
    }
}