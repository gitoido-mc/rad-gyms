/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui.screen

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.client.gui.CobblemonRenderable
import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.api.event.GymEvents.LEAVE_SCREEN_CLOSE
import lol.gito.radgyms.client.util.radGymsResource
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text.translatable
import net.minecraft.util.Colors
import org.lwjgl.glfw.GLFW


@Environment(EnvType.CLIENT)
class GymLeaveScreen : CobblemonRenderable, Screen(translatable(modId("gui.common.leave").toTranslationKey())) {
    companion object {
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        private val panelResource = radGymsResource("textures/gui/gym_leave.png")
    }

    val middleX: Int
        get() = this.client!!.window.scaledWidth / 2
    val middleY: Int
        get() = this.client!!.window.scaledHeight / 2

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    private var closeReason: GuiScreenCloseChoice = GuiScreenCloseChoice.CANCEL

    override fun applyBlur(delta: Float) {}

    override fun renderDarkening(context: DrawContext) {}

    override fun close() {
        LEAVE_SCREEN_CLOSE.emit(
            GymEvents.GymLeaveScreenCloseEvent(this.closeReason)
        )
    }

    fun close(reason: GuiScreenCloseChoice) {
        this.closeReason = reason
        this.close()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.close(GuiScreenCloseChoice.CANCEL)
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }


    override fun init() {
        val proceedButton = ButtonWidget
            .builder(ScreenTexts.PROCEED) {
                close(GuiScreenCloseChoice.PROCEED)
            }
            .size(50, 20)
            .position(leftX + 10, topY + (BASE_HEIGHT - 30))
            .build()

        val cancelButton = ButtonWidget
            .builder(ScreenTexts.CANCEL) { close(GuiScreenCloseChoice.CANCEL) }
            .size(50, 20)
            .position(leftX + (BASE_WIDTH - 60), topY + (BASE_HEIGHT - 30))
            .build()

        this.addDrawableChild(proceedButton)
        this.addDrawableChild(cancelButton)

        super.init()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val matrices = context.matrices
        super.renderInGameBackground(context)

        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        blitk(
            matrixStack = matrices,
            texture = panelResource,
            x = x, y = y,
            width = BASE_WIDTH,
            height = BASE_HEIGHT
        )

        // Box Label
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave").toTranslationKey()),
            x = x + (BASE_WIDTH / 2),
            y = y + 10,
            centered = true,
            colour = Colors.BLACK
        )
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave-gym").toTranslationKey()),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 16,
            centered = true,
            colour = Colors.BLACK
        )
        drawScaledText(
            context = context,
            text = translatable(modId("gui.common.leave-gym-reward").toTranslationKey()),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 4,
            centered = true,
            colour = Colors.BLACK
        )

        super.render(context, mouseX, mouseY, delta)
    }
}