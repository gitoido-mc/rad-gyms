/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.renderer

import lol.gito.radgyms.client.RadGymsClient
import lol.gito.radgyms.common.registry.DataComponentRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class GymKeyRenderer : SpecialItemRenderer() {
    companion object {
        val INSTANCE = GymKeyRenderer()
    }

    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        leftHanded: Boolean,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        var model = RadGymsClient.modModelId("gym_key_default", "inventory")
        val type = stack.get(DataComponentRegistry.GYM_TYPE_COMPONENT)
        val overrideModel = RadGymsClient.modModelId("gym_keys/$type", "inventory")
        val modelManager = MinecraftClient.getInstance().bakedModelManager

        if (modelManager.getModel(overrideModel) != modelManager.missingModel) {
            model = overrideModel
        }

        renderModel(model, stack, mode, leftHanded, matrices, vertexConsumers, light, overlay)
    }
}
