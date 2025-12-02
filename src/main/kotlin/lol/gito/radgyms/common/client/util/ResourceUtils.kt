/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.client.util

import lol.gito.radgyms.common.RadGyms.MOD_ID
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation


fun radGymsResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

@Suppress("unused")
fun radGymsModel(path: String, variant: String): ModelResourceLocation =
    ModelResourceLocation(radGymsResource(path), variant)
