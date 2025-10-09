/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client

import com.cobblemon.mod.common.api.Priority
import lol.gito.radgyms.api.events.GuiEvents
import lol.gito.radgyms.client.gui.screen.GymEnterScreen
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.network.handler.OpenGymEnterScreenS2CHandler
import lol.gito.radgyms.common.network.handler.OpenGymLeaveScreenS2CHandler
import lol.gito.radgyms.common.network.payload.OpenGymEnterScreenS2C
import lol.gito.radgyms.common.network.payload.OpenGymLeaveScreenS2C
import lol.gito.radgyms.common.registry.EntityRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.VillagerEntityRenderer
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.util.Identifier

object RadGymsClient {
    @Environment(EnvType.CLIENT)
    fun init() {
        debug("Initializing client")
        EntityRendererRegistry.register(EntityRegistry.GYM_TRAINER) { context ->
            VillagerEntityRenderer(context)
        }

        ClientPlayNetworking.registerGlobalReceiver(OpenGymEnterScreenS2C.ID, ::OpenGymEnterScreenS2CHandler)
        ClientPlayNetworking.registerGlobalReceiver(OpenGymLeaveScreenS2C.ID, ::OpenGymLeaveScreenS2CHandler)
        GuiEvents.ENTER_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            debug("Enter screen event emitted - lower prio - set max level to ${it.maxLevel}")
            MinecraftClient.getInstance().setScreen(
                GymEnterScreen(
                    it.key,
                    it.type,
                    it.pos,
                    it.minLevel,
                    it.maxLevel,
                )
            )
        }


        GuiEvents.ENTER_SCREEN_OPEN.subscribe(Priority.NORMAL) {
            debug("Enter screen event emitted - normal prio - set max level to 30")
            it.maxLevel = 30
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