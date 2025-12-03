package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.api.dto.Gym
import java.util.concurrent.ConcurrentHashMap

object RadGymsTemplates {
    private val templates: MutableMap<String, Gym.Json> = ConcurrentHashMap()

    fun clearTemplates() = templates.clear()

    fun registerTemplate(name: String, template: Gym.Json) {
        templates[name] = template
    }

    fun getTemplateOrDefault(type: String?): Gym.Json? {
        return when {
            type != null && templates.containsKey(type) -> templates[type]
            templates.containsKey("default") -> templates["default"]
            else -> null
        }
    }

    fun getTemplateIdentifiers(): Set<String> = templates.keys
}