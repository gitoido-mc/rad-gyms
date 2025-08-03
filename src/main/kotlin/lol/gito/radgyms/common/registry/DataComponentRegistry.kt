/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import com.mojang.serialization.Codec
import lol.gito.radgyms.common.RadGyms
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object DataComponentRegistry {
    val GYM_TYPE_COMPONENT: ComponentType<String> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RadGyms.modId("gym_type_component"),
        ComponentType.builder<String>().codec(Codec.STRING).build()
    )

    val RAD_GYM_BUNDLE_COMPONENT: ComponentType<Boolean> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RadGyms.modId("bundle"),
        ComponentType.builder<Boolean>().codec(Codec.BOOL).build()
    )

    val CACHE_SHINY_BOOST_COMPONENT: ComponentType<Int> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RadGyms.modId("shiny_boost"),
        ComponentType.builder<Int>().codec(Codec.INT).build()
    )

    fun register() {
        RadGyms.debug("Registering data components")
    }
}