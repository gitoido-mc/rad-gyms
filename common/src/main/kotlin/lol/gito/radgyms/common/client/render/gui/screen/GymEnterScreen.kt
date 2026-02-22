/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.ENTER_SCREEN_CLOSE
import lol.gito.radgyms.common.client.render.gui.widget.LevelSliderWidget
import lol.gito.radgyms.common.extension.math.Vec2i
import lol.gito.radgyms.common.helper.ElementalTypeTranslationHelper.buildTypeText
import lol.gito.radgyms.common.helper.tl
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors
import org.lwjgl.glfw.GLFW

@Suppress("LongParameterList")
class GymEnterScreen(
    val key: Boolean,
    val selectedLevel: Int,
    val minLevel: Int = RadGyms.config.minLevel!!,
    val maxLevel: Int = RadGyms.config.maxLevel!!,
    val type: String? = null,
    val pos: BlockPos? = null,
    val usesLeft: Int? = null,
) : AbstractGymScreen(
    when (type) {
        null, "chaos", (ElementalTypes.getOrException(type).showdownId) -> tl(
            "gui.common.set-gym-level",
            buildTypeText(type),
        )

        else -> tl("gui.common.set-custom-gym-level", buildTypeText(type))
    },
) {
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

        // +1 button widget params
        const val GUI_INC1_WIDTH = 20
        const val GUI_INC1_HEIGHT = 20
        const val GUI_INC1_OFFSET_X = 245
        const val GUI_INC1_OFFSET_Y = 25

        // +10 button widget params
        const val GUI_INC10_WIDTH = 25
        const val GUI_INC10_HEIGHT = 20
        const val GUI_INC10_OFFSET_X = 265
        const val GUI_INC10_OFFSET_Y = 25
        const val GUI_INC10_AMOUNT = 10

        // -1 button widget params
        const val GUI_DEC1_WIDTH = 20
        const val GUI_DEC1_HEIGHT = 20
        const val GUI_DEC1_OFFSET_X = 10
        const val GUI_DEC1_OFFSET_Y = 25

        // -10 button widget params
        const val GUI_DEC10_WIDTH = 25
        const val GUI_DEC10_HEIGHT = 20
        const val GUI_DEC10_OFFSET_X = 30
        const val GUI_DEC10_OFFSET_Y = 25
        const val GUI_DEC10_AMOUNT = 10

        // Level select slider widget params
        const val GUI_LSS_OFFSET_X = 55
        const val GUI_LSS_OFFSET_Y = 25

        private val panelResource = modId("textures/gui/gym_enter.png")
    }

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    var level: Int = this.selectedLevel.coerceIn(this.minLevel, this.maxLevel)

    @Suppress("DuplicatedCode", "LongMethod")
    override fun init() {
        val levelSelectSlider =
            LevelSliderWidget(
                x = leftX + GUI_LSS_OFFSET_X,
                y = topY + GUI_LSS_OFFSET_Y,
                initialLevel = this.level,
                minLevel = this.minLevel,
                maxLevel = this.maxLevel,
            ) { level ->
                this.level = level
                this.tick()
            }.also {
                this.addRenderableWidget(it)
            }

        createButton(
            Component.nullToEmpty("-1"),
            Vec2i(GUI_DEC1_WIDTH, GUI_DEC1_HEIGHT),
            Vec2i(leftX + GUI_DEC1_OFFSET_X, topY + GUI_DEC1_OFFSET_Y),
        ) {
            level = level.dec().coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("-10"),
            Vec2i(GUI_DEC10_WIDTH, GUI_DEC10_HEIGHT),
            Vec2i(leftX + GUI_DEC10_OFFSET_X, topY + GUI_DEC10_OFFSET_Y),
        ) {
            level = level.minus(GUI_DEC10_AMOUNT).coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("+1"),
            Vec2i(GUI_INC1_WIDTH, GUI_INC1_HEIGHT),
            Vec2i(leftX + GUI_INC1_OFFSET_X, topY + GUI_INC1_OFFSET_Y),
        ) {
            level = level.inc().coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("+10"),
            Vec2i(GUI_INC10_WIDTH, GUI_INC10_HEIGHT),
            Vec2i(leftX + GUI_INC10_OFFSET_X, topY + GUI_INC10_OFFSET_Y),
        ) {
            level = level.plus(GUI_INC10_AMOUNT).coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

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

        super.init()
    }

    override fun onClose() {
        ENTER_SCREEN_CLOSE.emit(
            GymEvents.GymEnterScreenCloseEvent(
                this.closeReason,
                this.key,
                this.level,
                this.type,
                this.pos,
            ),
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

        val message =
            when (pos) {
                null -> tl("gui.common.set-gym-level", buildTypeText(type))
                else ->
                    tl(
                        "gui.common.set-gym-level-entry",
                        buildTypeText(type),
                        usesLeft,
                    )
            }

        // Box Label
        drawScaledText(
            context = context,
            text = message,
            x = (width - BASE_WIDTH) / 2 + (BASE_WIDTH / 2),
            y = (height - BASE_HEIGHT) / 2 + 10,
            centered = true,
            colour = CommonColors.BLACK,
        )

        super.render(context, mouseX, mouseY, delta)
    }
}
