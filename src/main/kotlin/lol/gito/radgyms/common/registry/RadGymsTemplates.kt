/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

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