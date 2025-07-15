/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.world

import lol.gito.radgyms.RadGyms.MOD_ID
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World

object DimensionManager {
    val RADGYMS_LEVEL_KEY: RegistryKey<World> =
        RegistryKey.of(RegistryKeys.WORLD, modId("${MOD_ID}_dim"))

    fun register() {
        debug("Registering gym dimension")
    }
}
