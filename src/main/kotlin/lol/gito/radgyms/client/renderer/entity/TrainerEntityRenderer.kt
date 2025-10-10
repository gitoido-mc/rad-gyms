/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.renderer.entity

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.Trainer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class TrainerEntityRenderer(ctx: EntityRendererFactory.Context) :
    LivingEntityRenderer<Trainer, PlayerEntityModel<Trainer>>(
        ctx,
        PlayerEntityModel(ctx.getPart(EntityModelLayers.PLAYER), false),
        .5f
    ) {
    companion object {
        val TEXTURE: Identifier = modId("textures/npc/default_trainer.png")
    }

//    init {
//        this.addFeature(GymTrainerIdFeature(this))
//    }

    override fun getTexture(entity: Trainer): Identifier {
        return try {
            val id = entity.dataTracker.get(Trainer.GYM_ID)
            modId("textures/npc/${id}.png")
        } catch (e: Throwable) {
            RadGyms.LOGGER.warn("Cannot use texture ${entity.dataTracker.get(Trainer.GYM_ID)}", e)
            TEXTURE
        }
    }
}