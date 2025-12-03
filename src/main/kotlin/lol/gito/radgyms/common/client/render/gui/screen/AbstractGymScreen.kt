package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.client.gui.CobblemonRenderable
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.client.render.gui.screen.GymEnterScreen.Companion.BASE_HEIGHT
import lol.gito.radgyms.common.client.render.gui.screen.GymEnterScreen.Companion.BASE_WIDTH
import lol.gito.radgyms.common.util.math.Vec2i
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

abstract class AbstractGymScreen(heading: Component): CobblemonRenderable, Screen(heading) {
    val middleX: Int
        get() = this.minecraft!!.window.guiScaledWidth / 2
    val middleY: Int
        get() = this.minecraft!!.window.guiScaledHeight / 2

    // Disabling blurs
    override fun renderBlurredBackground(delta: Float) {}

    // Disabling background
    override fun renderMenuBackground(context: GuiGraphics) {}

    protected var closeReason: GuiScreenCloseChoice = GuiScreenCloseChoice.CANCEL

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
    protected fun createButton(
        label: Component,
        size: Vec2i,
        pos: Vec2i,
        callback: (Button) -> Unit
    ): Button = Button
        .builder(label, callback)
        .size(size.x, size.y)
        .pos(pos.x, pos.y)
        .build()

    fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float, panelResource: ResourceLocation) {
        val matrices = context.pose()
        super.renderTransparentBackground(context)

        blitk(
            matrixStack = matrices,
            texture = panelResource,
            x = (width - BASE_WIDTH) / 2, y = (height - BASE_HEIGHT) / 2,
            width = BASE_WIDTH,
            height = BASE_HEIGHT
        )

        super.render(context, mouseX, mouseY, delta)
    }
}