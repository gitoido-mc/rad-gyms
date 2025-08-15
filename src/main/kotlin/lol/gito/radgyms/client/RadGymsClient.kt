/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.EntityManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.VillagerEntityRenderer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object RadGymsClient {
    @Environment(EnvType.CLIENT)
    fun init() {
        RadGyms.debug("Initializing client")
        EntityRendererRegistry.register(EntityManager.GYM_TRAINER) { context ->
            VillagerEntityRenderer(context)
        }
    }

    @Environment(EnvType.CLIENT)
    fun modModelId(id: Identifier, variant: String): ModelIdentifier {
        return ModelIdentifier(id, variant)
    }


    @Environment(EnvType.CLIENT)
    fun modModelId(name: String, variant: String): ModelIdentifier {
        return modModelId(modId(name), variant)
    }
}