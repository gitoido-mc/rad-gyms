/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui

import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.DiscreteSliderComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_GYM_SLIDER
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.UI_GYM_ENTER
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

open class GymEnterScreen(
    open val type: String? = null, open val id: Identifier = UI_GYM_ENTER, open val player: PlayerEntity
) :
    BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(id)),
    IGymEnterScreen {
    override lateinit var root: FlowLayout

    override var gymLevel: Double = 1.0
        set(value) {
            field = value.coerceIn(1.0, 100.0) // min and max limit
        }

    override fun build(root: FlowLayout) {
        this.root = root
        GymScreenCommons.enterScreenControls(root, this)
    }

    override fun close() {
        super.close()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun incPress(value: Double) {
        gymLevel += value
        updateSlider()
    }

    override fun decPress(value: Double) {
        gymLevel -= value
        updateSlider()
    }

    override fun updateSlider() {
        root.childById(DiscreteSliderComponent::class.java, ID_GYM_SLIDER).setFromDiscreteValue(gymLevel)
    }

    override fun sendStartGymPacket() {
        try {
            var level: Int = this.gymLevel.toInt()
            if (level < 5) {
                level = 5
            }

            val chosenType = when (type) {
                null -> ElementalTypes.all().random().name
                else -> type
            }

            this.close()

            debug("Sending GymEnterWithoutCoords(level:$level, type:$type) C2S packet from ${player.name}")

            CHANNEL.clientHandle().send(
                chosenType?.let {
                    NetworkStackHandler.GymEnterWithoutCoords(
                        level = level, type = it
                    )
                }
            )
        } catch (e: Exception) {
            LOGGER.warn(e.toString())
        }
    }
}
