package lol.gito.radgyms.gym

import com.cobblemon.mod.common.util.endsWith
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import lol.gito.radgyms.RadGyms
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import java.io.File

class GymLoader {
    companion object GymsResourceReloadListener : SimpleSynchronousResourceReloadListener {
        override fun getFabricId(): Identifier {
            return RadGyms.modIdentifier("gyms")
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun reload(manager: ResourceManager) {
            GymManager.GYM_TEMPLATES.clear()
            SpeciesManager.SPECIES_BY_TYPE.clear()
            manager.findResources("gyms") { path -> path.endsWith(".json") }
                .forEach { (id: Identifier, res: Resource) ->
                    try {
                        val templateName = File(id.path).nameWithoutExtension
                        GymManager.GYM_TEMPLATES[templateName] = Json.decodeFromStream<GymDTO>(res.inputStream)
                        RadGyms.LOGGER.info("Loaded $templateName template from ${File(id.path).name} gym config")
                    } catch (e: Exception) {
                        RadGyms.LOGGER.warn("Could not parse ${File(id.path).name} gym data", e)
                    }
                }
        }
    }

    fun register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(GymsResourceReloadListener)
    }
}