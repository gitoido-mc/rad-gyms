/*
 * Copyright (c) 2025. gitoido-mc
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
import lol.gito.radgyms.common.util.TranslationUtil.buildTypeText
import lol.gito.radgyms.common.util.math.Vec2i
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.util.CommonColors
import org.lwjgl.glfw.GLFW

class GymEnterScreen(
    val key: Boolean,
    val selectedLevel: Int,
    val minLevel: Int = RadGyms.CONFIG.minLevel!!,
    val maxLevel: Int = RadGyms.CONFIG.maxLevel!!,
    val type: String? = null,
    val pos: BlockPos? = null,
    val usesLeft: Int? = null,

    ) : AbstractGymScreen(
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

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    var level: Int = this.selectedLevel.coerceIn(this.minLevel, this.maxLevel)

    @Suppress("DuplicatedCode")
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
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("-1"),
            Vec2i(20, 20),
            Vec2i(leftX + 10, topY + 25)
        ) {
            level = level.dec().coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("-10"),
            Vec2i(25, 20),
            Vec2i(leftX + 30, topY + 25)
        ) {
            level = level.minus(10).coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("+1"),
            Vec2i(20, 20),
            Vec2i(leftX + 245, topY + 25),
        )  {
            level = level.inc().coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            Component.nullToEmpty("+10"),
            Vec2i(25, 20),
            Vec2i(leftX + 265, topY + 25)
        ) {
            level = level.plus(10).coerceIn(this.minLevel, this.maxLevel)
            levelSelectSlider.updateLevel(level)
        }.also {
            this.addRenderableWidget(it)
        }

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

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose(GuiScreenCloseChoice.CANCEL)
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.preRender(context, panelResource)

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
            x = (width - BASE_WIDTH) / 2 + (BASE_WIDTH / 2),
            y = (height - BASE_HEIGHT) / 2 + 10,
            centered = true,
            colour = CommonColors.BLACK
        )


        super.render(context, mouseX, mouseY, delta)
    }
}