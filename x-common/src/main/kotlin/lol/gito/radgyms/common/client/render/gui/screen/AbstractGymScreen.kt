/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.client.gui.CobblemonRenderable
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.extension.math.Vec2i
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.lwjgl.glfw.GLFW

abstract class AbstractGymScreen(heading: Component) :
    Screen(heading),
    CobblemonRenderable {
    companion object {
        // Screen root widget params
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        // Proceed button widget params
        const val GUI_PROCEED_WIDTH = 50
        const val GUI_PROCEED_HEIGHT = 20
        const val GUI_PROCEED_OFFSET_X = 10
        const val GUI_PROCEED_OFFSET_Y = 30

        // Cancel button widget params
        const val GUI_CANCEL_WIDTH = 50
        const val GUI_CANCEL_HEIGHT = 20
        const val GUI_CANCEL_OFFSET_X = 60
        const val GUI_CANCEL_OFFSET_Y = 30
    }

    protected var closeReason: GuiScreenCloseChoice = GuiScreenCloseChoice.CANCEL

    val middleX: Int
        get() = this.minecraft!!.window.guiScaledWidth / 2
    val middleY: Int
        get() = this.minecraft!!.window.guiScaledHeight / 2
    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    // Disabling blurs
    override fun renderBlurredBackground(delta: Float) = Unit

    // Disabling background
    override fun renderMenuBackground(context: GuiGraphics) = Unit

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose(GuiScreenCloseChoice.CANCEL)
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    fun onClose(reason: GuiScreenCloseChoice) {
        this.closeReason = reason
        this.onClose()
    }

    /**
     * Button creation helper function
     *
     * @param label Accepts `Component` instance with screen heading
     * @param size `Vec2i` holder for button size dimensions
     * @param pos `Vec2i` holder for button position
     * @param callback Callback function which fires on button click
     * @return Button
     */
    protected fun createButton(label: Component, size: Vec2i, pos: Vec2i, callback: (Button) -> Unit): Button = Button
        .builder(label, callback)
        .size(size.x, size.y)
        .pos(pos.x, pos.y)
        .build()

    fun preRender(context: GuiGraphics, panelResource: ResourceLocation) {
        val matrices = context.pose()
        super.renderTransparentBackground(context)

        blitk(
            matrixStack = matrices,
            texture = panelResource,
            x = (width - BASE_WIDTH) / 2,
            y = (height - BASE_HEIGHT) / 2,
            width = BASE_WIDTH,
            height = BASE_HEIGHT,
        )
    }

    fun addDecisionButtons() {
        createButton(
            CommonComponents.GUI_PROCEED,
            Vec2i(GUI_PROCEED_WIDTH, GUI_PROCEED_HEIGHT),
            Vec2i(
                leftX + GUI_PROCEED_OFFSET_X,
                topY + (BASE_HEIGHT - GUI_PROCEED_OFFSET_Y),
            ),
        ) {
            onClose(GuiScreenCloseChoice.PROCEED)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            CommonComponents.GUI_CANCEL,
            Vec2i(GUI_CANCEL_WIDTH, GUI_CANCEL_HEIGHT),
            Vec2i(
                leftX + (BASE_WIDTH - GUI_CANCEL_OFFSET_X),
                topY + (BASE_HEIGHT - GUI_CANCEL_OFFSET_Y),
            ),
        ) {
            onClose(GuiScreenCloseChoice.CANCEL)
        }.also {
            this.addRenderableWidget(it)
        }
    }
}
