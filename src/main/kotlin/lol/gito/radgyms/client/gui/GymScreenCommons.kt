package lol.gito.radgyms.client.gui

import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.component.DiscreteSliderComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_CANCEL
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_DEC
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_DEC_TEN
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_GYM_SLIDER
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_INC
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_INC_TEN
import lol.gito.radgyms.client.gui.GymGUIIdentifiers.ID_START

object GymScreenCommons {
    fun enterScreenControls(root: FlowLayout, screen: IGymEnterScreen) {
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
