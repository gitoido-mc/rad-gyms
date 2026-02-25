/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.entity

import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.Trainer
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.resources.ResourceLocation
import java.io.FileNotFoundException

class TrainerEntityRenderer(val ctx: EntityRendererProvider.Context) :
    LivingEntityRenderer<Trainer, PlayerModel<Trainer>>(
        ctx,
        PlayerModel(ctx.bakeLayer(ModelLayers.PLAYER), false),
        SHADOW_RADIUS,
    ) {
    companion object {
        const val SHADOW_RADIUS = .5f

        val DEFAULT_TEXTURE: ResourceLocation = modId("textures/npc/default_trainer.png")
    }

    override fun getTextureLocation(entity: Trainer): ResourceLocation = try {
        val id = modId("textures/npc/${entity.entityData.get(Trainer.GYM_ID)}.png")
        ctx.resourceManager.getResourceOrThrow(id)

        debug(
            "Cannot find texture {}, falling back to {}",
            modId("textures/npc/${entity.entityData.get(Trainer.GYM_ID)}.png").toString(),
            DEFAULT_TEXTURE.toString(),
        )

        id
    } catch (_: FileNotFoundException) {
        DEFAULT_TEXTURE
    }
}
