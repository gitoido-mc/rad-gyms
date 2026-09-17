/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.helper

import lol.gito.radgyms.common.RadGyms.MOD_ID
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation

fun modResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

@Suppress("unused")
fun modModel(path: String, variant: String): ModelResourceLocation = ModelResourceLocation(modResource(path), variant)
