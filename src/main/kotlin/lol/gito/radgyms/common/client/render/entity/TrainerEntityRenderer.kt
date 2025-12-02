/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.client.render.entity

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.Trainer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
class TrainerEntityRenderer(ctx: EntityRendererProvider.Context) :
    LivingEntityRenderer<Trainer, PlayerModel<Trainer>>(
        ctx,
        PlayerModel(ctx.bakeLayer(ModelLayers.PLAYER), false),
        .5f
    ) {

    companion object {
        val TEXTURE: ResourceLocation = modId("textures/npc/default_trainer.png")
    }

    override fun getTextureLocation(entity: Trainer): ResourceLocation {
        return try {
            val id = entity.entityData.get(Trainer.GYM_ID)
            modId("textures/npc/${id}.png")
        } catch (e: Throwable) {
            RadGyms.LOGGER.warn("Cannot use texture ${entity.entityData.get(Trainer.GYM_ID)}", e)
            TEXTURE
        }
    }
}