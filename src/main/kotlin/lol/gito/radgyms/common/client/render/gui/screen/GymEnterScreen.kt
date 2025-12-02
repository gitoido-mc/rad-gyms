/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.client.gui.CobblemonRenderable
import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.ENTER_SCREEN_CLOSE
import lol.gito.radgyms.common.client.render.gui.widget.LevelSliderWidget
import lol.gito.radgyms.common.util.TranslationUtil.buildTypeText
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.util.CommonColors
import org.lwjgl.glfw.GLFW


@Environment(EnvType.CLIENT)
class GymEnterScreen(
    val key: Boolean,
    val selectedLevel: Int,
    val minLevel: Int = RadGyms.CONFIG.minLevel!!,
    val maxLevel: Int = RadGyms.CONFIG.maxLevel!!,
    val type: String? = null,
    val pos: BlockPos? = null,
    val usesLeft: Int? = null,

) : CobblemonRenderable,
    Screen(
        when {
            (type == null || ElementalTypes.get(type) != null || type == "chaos") -> translatable(
                modId("gui.common.set-gym-level").toLanguageKey(),
                buildTypeText(type)
            )

            else -> translatable(
                modId("gui.common.set-custom-gym-level").toLanguageKey(),
                buildTypeText(type)
            )
        }
    ) {
    companion object {
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        private val panelResource = modId("textures/gui/gym_enter.png")
    }

    val middleX: Int
        get() = this.minecraft!!.window.guiScaledWidth / 2
    val middleY: Int
        get() = this.minecraft!!.window.guiScaledHeight / 2

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    var level: Int = this.selectedLevel.coerceIn(this.minLevel, this.maxLevel)

    private var closeReason: GuiScreenCloseChoice = GuiScreenCloseChoice.CANCEL

    override fun renderBlurredBackground(delta: Float) {}

    override fun renderMenuBackground(context: GuiGraphics) {}

    override fun onClose() {
        ENTER_SCREEN_CLOSE.emit(
            GymEvents.GymEnterScreenCloseEvent(
                this.closeReason,
                this.key,
                this.level,
                this.type,
                this.pos
            )
        )
    }

    fun onClose(reason: GuiScreenCloseChoice) {
        this.closeReason = reason
        this.onClose()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose(GuiScreenCloseChoice.CANCEL)
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun init() {
        val levelSelectSlider = LevelSliderWidget(
            x = leftX + 55,
            y = topY + 25,
            initialLevel = this.level,
            minLevel = this.minLevel,
            maxLevel = this.maxLevel,
        ) { level ->
            this.level = level
            this.tick()
        }

        val decButton = Button
            .builder(Component.nullToEmpty("-1")) {
                level = level.dec().coerceIn(this.minLevel, this.maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(20, 20)
            .pos(leftX + 10, topY + 25)
            .build()

        val dec10Button = Button
            .builder(Component.nullToEmpty("-10")) {
                level = level.minus(10).coerceIn(this.minLevel, this.maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(25, 20)
            .pos(leftX + 30, topY + 25)
            .build()

        val incButton = Button
            .builder(Component.nullToEmpty("+1")) {
                level = level.inc().coerceIn(this.minLevel, this.maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(20, 20)
            .pos(leftX + 245, topY + 25)
            .build()

        val inc10Button = Button
            .builder(Component.nullToEmpty("+10")) {
                level = level.plus(10).coerceIn(this.minLevel, this.maxLevel)
                levelSelectSlider.updateLevel(level)
            }
            .size(25, 20)
            .pos(leftX + 265, topY + 25)
            .build()

        val proceedButton = Button
            .builder(CommonComponents.GUI_PROCEED) {
                onClose(GuiScreenCloseChoice.PROCEED)
            }
            .size(50, 20)
            .pos(leftX + 10, topY + (BASE_HEIGHT - 30))
            .build()

        val cancelButton = Button
            .builder(CommonComponents.GUI_CANCEL) {
                onClose(GuiScreenCloseChoice.CANCEL)
            }
            .size(50, 20)
            .pos(leftX + (BASE_WIDTH - 60), topY + (BASE_HEIGHT - 30))
            .build()

        this.addRenderableWidget(levelSelectSlider)
        this.addRenderableWidget(decButton)
        this.addRenderableWidget(dec10Button)
        this.addRenderableWidget(incButton)
        this.addRenderableWidget(inc10Button)
        this.addRenderableWidget(proceedButton)
        this.addRenderableWidget(cancelButton)

        super.init()
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        val matrices = context.pose()
        super.renderTransparentBackground(context)

        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        blitk(
            matrixStack = matrices,
            texture = panelResource,
            x = x, y = y,
            width = BASE_WIDTH,
            height = BASE_HEIGHT
        )

        val message = when (pos) {
            null -> translatable("rad-gyms.gui.common.set-gym-level", buildTypeText(type))
            else -> {
                translatable(
                    "rad-gyms.gui.common.set-gym-level-entry",
                    buildTypeText(type),
                    usesLeft
                )
            }
        }

        // Box Label
        drawScaledText(
            context = context,
            text = message,
            x = x + (BASE_WIDTH / 2),
            y = y + 10,
            centered = true,
            colour = CommonColors.BLACK
        )

        super.render(context, mouseX, mouseY, delta)
    }
}