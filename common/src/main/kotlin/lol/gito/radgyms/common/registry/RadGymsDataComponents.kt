/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import com.cobblemon.mod.common.platform.PlatformRegistry
import com.mojang.serialization.Codec
import lol.gito.radgyms.common.RadGyms
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

private typealias DCTRegistry = Registry<DataComponentType<*>>
private typealias DCTRegistryKey = ResourceKey<Registry<DataComponentType<*>>>

object RadGymsDataComponents : PlatformRegistry<DCTRegistry, DCTRegistryKey, DataComponentType<*>>() {
    override val registry: DCTRegistry = BuiltInRegistries.DATA_COMPONENT_TYPE
    override val resourceKey: DCTRegistryKey = Registries.DATA_COMPONENT_TYPE

    @JvmField
    val RG_GYM_TYPE_COMPONENT: DataComponentType<String> = this.create(
        RadGyms.modId("gym_type"),
        DataComponentType.builder<String>().persistent(Codec.STRING).build()
    )

    @JvmField
    val RG_CACHE_SHINY_BOOST_COMPONENT: DataComponentType<Int> = this.create(
        RadGyms.modId("shiny_boost"),
        DataComponentType.builder<Int>().persistent(Codec.INT).build()
    )
}
