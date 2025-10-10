/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.renderer.entity.feature

import lol.gito.radgyms.client.renderer.entity.TrainerEntityRenderer
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.Trainer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class GymTrainerIdFeature(ctx: FeatureRendererContext<Trainer, PlayerEntityModel<Trainer>>) :
    FeatureRenderer<Trainer, PlayerEntityModel<Trainer>>(ctx) {
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: Trainer,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if (!entity.isInvisible) {
            val texture: Identifier = modId("textures/npc/${entity.gymId}.png")
            if (texture.path != TrainerEntityRenderer.TEXTURE.path) {
                debug(texture.toString())
                renderModel(contextModel, texture, matrices, vertexConsumers, light, entity, -1)
            }
        }
    }
}