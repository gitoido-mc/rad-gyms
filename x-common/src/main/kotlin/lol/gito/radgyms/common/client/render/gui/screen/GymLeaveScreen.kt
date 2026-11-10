/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.gui.screen

import com.cobblemon.mod.common.client.render.drawScaledText
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.LEAVE_SCREEN_CLOSE
import lol.gito.radgyms.common.extension.math.Vec2i
import lol.gito.radgyms.common.helper.modResource
import lol.gito.radgyms.common.helper.tl
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.CommonComponents
import net.minecraft.util.CommonColors

class GymLeaveScreen : AbstractGymScreen(tl(modId("gui.common.leave"))) {
    companion object {
        private val panelResource = modResource("textures/gui/gym_leave.png")
    }

    override fun onClose() = LEAVE_SCREEN_CLOSE.emit(GymEvents.GymLeaveScreenCloseEvent(this.closeReason))

    @Suppress("DuplicatedCode")
    override fun init() {
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

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.preRender(context, panelResource)
        val x = (width - BASE_WIDTH) / 2
        val y = (height - BASE_HEIGHT) / 2

        // Box Label
        drawScaledText(
            context = context,
            text = tl(modId("gui.common.leave")),
            x = x + (BASE_WIDTH / 2),
            y = y + 10,
            centered = true,
            colour = CommonColors.BLACK,
        )
        drawScaledText(
            context = context,
            text = tl(modId("gui.common.leave_gym")),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 16,
            centered = true,
            colour = CommonColors.BLACK,
        )
        drawScaledText(
            context = context,
            text = tl(modId("gui.common.leave_gym_reward")),
            x = x + (BASE_WIDTH / 2),
            y = middleY - 4,
            centered = true,
            colour = CommonColors.BLACK,
        )

        super.render(context, mouseX, mouseY, delta)
    }
}
