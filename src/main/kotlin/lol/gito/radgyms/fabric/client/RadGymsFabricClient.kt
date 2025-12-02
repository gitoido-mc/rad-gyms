/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.client

import com.cobblemon.mod.common.api.Priority
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.client.render.entity.TrainerEntityRenderer
import lol.gito.radgyms.common.client.render.gui.screen.GymEnterScreen
import lol.gito.radgyms.common.client.render.gui.screen.GymLeaveScreen
import lol.gito.radgyms.common.network.client.payload.GymEnterC2S
import lol.gito.radgyms.common.network.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsEntities
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation

@Environment(EnvType.CLIENT)
object RadGymsFabricClient {
    fun modModelId(id: ResourceLocation, variant: String): ModelResourceLocation = ModelResourceLocation(id, variant)

    fun init() {
        RadGyms.debug("Initializing client")
        BlockRenderLayerMap.INSTANCE.putBlock(RadGymsBlocks.GYM_ENTRANCE, RenderType.cutout())

        EntityRendererRegistry.register(RadGymsEntities.GYM_TRAINER) { context ->
            TrainerEntityRenderer(context)
        }

        GymEvents.ENTER_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            Minecraft.getInstance().setScreen(
                GymEnterScreen(
                    it.key,
                    it.selectedLevel,
                    it.minLevel,
                    it.maxLevel,
                    it.type,
                    it.pos,
                )
            )
        }
        GymEvents.ENTER_SCREEN_CLOSE.subscribe(Priority.LOWEST) {
            if (it.choice == GuiScreenCloseChoice.PROCEED) {
                GymEnterC2S(it.key, it.level, it.type, it.pos).sendToServer()
            }
            Minecraft.getInstance().setScreen(null)
        }

        GymEvents.LEAVE_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            Minecraft.getInstance().setScreen(GymLeaveScreen())
        }

        GymEvents.LEAVE_SCREEN_CLOSE.subscribe(Priority.LOWEST) {
            if (it.choice == GuiScreenCloseChoice.PROCEED) {
                ClientPlayNetworking.send(GymLeaveC2S(true))
            }
            Minecraft.getInstance().setScreen(null)
        }
    }
}