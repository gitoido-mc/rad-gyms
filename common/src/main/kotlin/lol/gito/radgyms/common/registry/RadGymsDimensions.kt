/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.registry

import lol.gito.radgyms.common.RadGyms
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

/**
 * Just need a key here because dimension registry is dynamic
 */
object RadGymsDimensions {
    @JvmField
    val RADGYMS_LEVEL_KEY: ResourceKey<Level> = ResourceKey.create(
        Registries.DIMENSION,
        RadGyms.modId("${RadGyms.MOD_ID}_dim")
    )
}
