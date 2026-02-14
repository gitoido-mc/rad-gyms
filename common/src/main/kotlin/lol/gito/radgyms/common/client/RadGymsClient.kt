/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client

import com.cobblemon.mod.common.api.Priority
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.client.render.entity.TrainerEntityRenderer
import lol.gito.radgyms.common.client.render.gui.screen.GymEnterScreen
import lol.gito.radgyms.common.client.render.gui.screen.GymLeaveScreen
import lol.gito.radgyms.common.net.client.payload.GymEnterC2S
import lol.gito.radgyms.common.net.client.payload.GymLeaveC2S
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsEntities
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation

object RadGymsClient {
    @JvmStatic
    fun modModelId(id: ResourceLocation, variant: String): ModelResourceLocation = ModelResourceLocation(id, variant)

    lateinit var implementation: RadGymsClientImplementation

    fun initialize(implementation: RadGymsClientImplementation) {
        this.implementation = implementation
        this.registerBlockRenderTypes()
        this.registerEntityRenderers()
        this.registerClientEvents()
    }

    private fun registerBlockRenderTypes() {
        this.implementation.registerBlockRenderType(
            RenderType.cutout(),
            RadGymsBlocks.GYM_ENTRANCE
        )
    }

    private fun registerEntityRenderers() {
        this.implementation.registerEntityRenderer(RadGymsEntities.GYM_TRAINER, ::TrainerEntityRenderer)
    }

    private fun registerClientEvents() {
        GymEvents.ENTER_SCREEN_OPEN.subscribe(Priority.LOWEST) {
            Minecraft.getInstance().setScreen(
                GymEnterScreen(
                    it.key,
                    it.selectedLevel,
                    it.minLevel,
                    it.maxLevel,
                    it.type,
                    it.pos,
                    it.usesLeft
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
                GymLeaveC2S(true).sendToServer()
            }
            Minecraft.getInstance().setScreen(null)
        }
    }
}
