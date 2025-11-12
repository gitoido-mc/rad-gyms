/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms

import com.cobblemon.mod.common.api.Priority
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.api.event.ModEvents
import lol.gito.radgyms.common.network.payload.GymEnterC2S
import lol.gito.radgyms.common.network.payload.GymLeaveC2S
import lol.gito.radgyms.common.network.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.BlockRegistry
import lol.gito.radgyms.common.registry.EntityRegistry
import lol.gito.radgyms.common.registry.EventRegistry.ENTER_SCREEN_CLOSE
import lol.gito.radgyms.common.registry.EventRegistry.ENTER_SCREEN_OPEN
import lol.gito.radgyms.common.registry.EventRegistry.LEAVE_SCREEN_CLOSE
import lol.gito.radgyms.common.registry.EventRegistry.LEAVE_SCREEN_OPEN
import lol.gito.radgyms.gui.screen.GymEnterScreen
import lol.gito.radgyms.gui.screen.GymLeaveScreen
import lol.gito.radgyms.render.entity.TrainerEntityRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object RadGymsClient {
    fun modModelId(id: Identifier, variant: String): ModelIdentifier = ModelIdentifier(id, variant)

    fun init() {
        debug("Initializing client")
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.GYM_ENTRANCE, RenderLayer.getCutout())

        EntityRendererRegistry.register(EntityRegistry.GYM_TRAINER) { context ->
            TrainerEntityRenderer(context)
        }

        ClientPlayNetworking.registerGlobalReceiver(OpenGymEnterScreenS2C.Companion.ID) { payload, context ->
            ENTER_SCREEN_OPEN.emit(
                ModEvents.GymEnterScreenOpenEvent(
                    payload.pos,
                    payload.key,
                    payload.type,
                    RadGyms.CONFIG.minLevel!!,
                    RadGyms.CONFIG.maxLevel!!,
                    payload.derivedLevel
                )
            )
        }
        ClientPlayNetworking.registerGlobalReceiver(OpenGymLeaveScreenS2C.Companion.ID) { payload, context ->
            LEAVE_SCREEN_OPEN.emit(
                ModEvents.GymLeaveScreenOpenEvent(payload.id)
            )
        }
        ENTER_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            MinecraftClient.getInstance().setScreen(
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
        ENTER_SCREEN_CLOSE.subscribe(Priority.LOWEST) {
            if (it.choice == GuiScreenCloseChoice.PROCEED) {
                ClientPlayNetworking.send(
                    GymEnterC2S(
                        it.key,
                        it.level,
                        it.type,
                        it.pos
                    )
                )
            }
            MinecraftClient.getInstance().setScreen(null)
        }

        LEAVE_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            MinecraftClient.getInstance().setScreen(GymLeaveScreen())
        }

        LEAVE_SCREEN_CLOSE.subscribe(Priority.LOWEST) {
            if (it.choice == GuiScreenCloseChoice.PROCEED) {
                ClientPlayNetworking.send(GymLeaveC2S(true))
            }
            MinecraftClient.getInstance().setScreen(null)
        }
    }
}