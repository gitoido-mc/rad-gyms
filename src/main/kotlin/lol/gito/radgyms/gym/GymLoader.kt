package lol.gito.radgyms.gym

import com.cobblemon.mod.common.util.endsWith
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.resource.Gym
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

class GymLoader {
    companion object GymsResourceReloadListener: SimpleSynchronousResourceReloadListener {
        override fun getFabricId(): Identifier {
            return RadGyms.modIdentifier("gyms")
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun reload(manager: ResourceManager) {
            GymManager.GYM_TEMPLATES.clear()
            for (item in manager.findResources("gyms") { path -> path.endsWith(".json") }) {
                try {
                    val stream = manager.getResourceOrThrow(item.key).inputStream
                    GymManager.GYM_TEMPLATES[item.key.toString()] = Json.decodeFromStream<Gym>(stream)
                    RadGyms.LOGGER.info("Loaded ${item.key} gym config")
                } catch (e: Exception){
                    RadGyms.LOGGER.warn("Could not parse ${item.key} data", e)
                }
            }
        }

    }

    fun register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GymsResourceReloadListener)
    }
}