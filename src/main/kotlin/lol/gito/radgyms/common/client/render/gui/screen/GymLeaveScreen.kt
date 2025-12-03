/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.LEAVE_SCREEN_CLOSE
import lol.gito.radgyms.common.util.math.Vec2i
import lol.gito.radgyms.common.util.radGymsResource
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component.translatable
import net.minecraft.util.CommonColors
import org.lwjgl.glfw.GLFW

@Environment(EnvType.CLIENT)
class GymLeaveScreen : AbstractGymScreen(translatable(modId("gui.common.leave").toLanguageKey())) {
    companion object {
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        private val panelResource = radGymsResource("textures/gui/gym_leave.png")
    }

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    @Suppress("DuplicatedCode")
    override fun init() {
        createButton(
            CommonComponents.GUI_PROCEED,
            Vec2i(50, 20),
            Vec2i(leftX + 10, topY + (BASE_HEIGHT - 30))
        ) {
            onClose(GuiScreenCloseChoice.PROCEED)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            CommonComponents.GUI_CANCEL,
            Vec2i(50, 20),
            Vec2i(leftX + (BASE_WIDTH - 60), topY + (BASE_HEIGHT - 30))
        ) {
            onClose(GuiScreenCloseChoice.CANCEL)
        }.also {
            this.addRenderableWidget(it)
        }

        super.init()
    }

    override fun onClose() {
        LEAVE_SCREEN_CLOSE.emit(
            GymEvents.GymLeaveScreenCloseEvent(this.closeReason)
        )
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose(GuiScreenCloseChoice.CANCEL)
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.preRender(context, panelResource)
        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        // Box Label
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave").toLanguageKey()),
            x = x + (BASE_WIDTH / 2),
            y = y + 10,
            centered = true,
            colour = CommonColors.BLACK
        )
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave-gym").toLanguageKey()),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 16,
            centered = true,
            colour = CommonColors.BLACK
        )
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave-gym-reward").toLanguageKey()),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 4,
            centered = true,
            colour = CommonColors.BLACK
        )


        super.render(context, mouseX, mouseY, delta)
    }
}