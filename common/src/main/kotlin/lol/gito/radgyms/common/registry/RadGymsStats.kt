package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.stats.StatFormatter

object RadGymsStats {
    val store = mutableMapOf<String, RadGymsStat>()

    @JvmField
    val GYMS_VISITED: RadGymsStat = RadGymsStat("gyms_visited")

    @JvmField
    val GYMS_BEATEN: RadGymsStat = RadGymsStat("gyms_beaten")

    @JvmField
    val GYMS_FAILED: RadGymsStat = RadGymsStat("gyms_failed")

    @JvmField
    val KEYS_USED: RadGymsStat = RadGymsStat("keys_used")

    @JvmField
    val ENTRANCES_USED: RadGymsStat = RadGymsStat("entrances_used")

    @JvmField
    val ROPES_USED: RadGymsStat = RadGymsStat("ropes_used")

    @JvmField
    val CACHES_OPENED: RadGymsStat = RadGymsStat("caches_opened")

    fun registerStats() {
        store["gyms_visited"] = GYMS_VISITED
        store["gyms_beaten"] = GYMS_BEATEN
        store["gyms_failed"] = GYMS_FAILED
        store["entrances_used"] = ENTRANCES_USED
        store["keys_used"] = KEYS_USED
        store["ropes_used"] = ROPES_USED
        store["caches_opened"] = CACHES_OPENED
    }

    @JvmStatic
    fun getStat(radGymsStat: RadGymsStat): ResourceLocation {
        val resourceLocation = radGymsStat.resourceLocation
        val stat = BuiltInRegistries.CUSTOM_STAT.get(resourceLocation)
        if (stat == null) {
            RadGyms.warn("Could not find stat with id ${resourceLocation}}")
        }
        return stat ?: throw NullPointerException("Could not find stat with id ${resourceLocation}}")
    }

    data class RadGymsStat(val path: String,  val formatter: StatFormatter = StatFormatter.DEFAULT) {
        val resourceLocation: ResourceLocation = RadGyms.modId(path)
    }
}
