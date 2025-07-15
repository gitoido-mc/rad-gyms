/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.client.gui

import com.cobblemon.mod.common.util.cobblemonResource
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.component.DiscreteSliderComponent
import io.wispforest.owo.ui.component.LabelComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_CANCEL
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_DEC
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_DEC_TEN
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_GYM_SLIDER
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_INC
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_INC_TEN
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_START
import lol.gito.radgyms.client.gui.GymGuiIdentifiers.ID_TYPING
import net.minecraft.text.Text.translatable

object GymScreenCommons {
    fun gymScreenCommonControls(root: FlowLayout, screen: IGymEnterScreen) {
        val typingLabel = root.childById(LabelComponent::class.java, ID_TYPING)
        val intermediate = modId("gui.common.set-gym-level")
        val gymType = when (screen.getGymType()) {
            null -> modId("item.component.type.chaos").toTranslationKey()
            else -> cobblemonResource("type.${screen.getGymType()}").toTranslationKey()
        }
        typingLabel.text(
            translatable(
                intermediate.toTranslationKey(),
                gymType
            )
        )

        root.childById(DiscreteSliderComponent::class.java, ID_GYM_SLIDER).setFromDiscreteValue(screen.gymLevel)
        root.childById(DiscreteSliderComponent::class.java, ID_GYM_SLIDER).onChanged().subscribe {
            screen.apply {
                gymLevel = it
            }
        }
        root.childById(ButtonComponent::class.java, ID_INC).onPress {
            screen.incPress(1.0)
        }
        root.childById(ButtonComponent::class.java, ID_INC_TEN).onPress {
            screen.incPress(10.0)
        }
        root.childById(ButtonComponent::class.java, ID_DEC).onPress {
            screen.decPress(1.0)
        }
        root.childById(ButtonComponent::class.java, ID_DEC_TEN).onPress {
            screen.decPress(10.0)
        }
        root.childById(ButtonComponent::class.java, ID_CANCEL).onPress {
            screen.close()
        }
        root.childById(ButtonComponent::class.java, ID_START).onPress {
            screen.sendStartGymPacket()
        }
    }
}
