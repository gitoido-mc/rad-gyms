/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World

object DimensionRegistry {
    val RADGYMS_LEVEL_KEY: RegistryKey<World> =
        RegistryKey.of(RegistryKeys.WORLD, RadGyms.modId("${RadGyms.MOD_ID}_dim"))

    fun register() {
        RadGyms.debug("Registering gym dimension")
    }
}