package lol.gito.radgyms.gui

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.component.DiscreteSliderComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.network.NetworkStackProcessor
import net.minecraft.entity.player.PlayerEntity
import org.lwjgl.glfw.GLFW

class GymKeyScreen(val player: PlayerEntity) : BaseUIModelScreen<FlowLayout>(
    FlowLayout::class.java,
    DataSource.asset(RadGyms.modIdentifier("gym_key_ui"))
) {
    private var gymLevel: Double = 1.0
        set(value) {
            field = value.coerceIn(1.0, 100.0) // min and max limit
        };
    private lateinit var root: FlowLayout;

    override fun build(root: FlowLayout) {
        this.root = root;
        root.childById(DiscreteSliderComponent::class.java, "gym_slider").setFromDiscreteValue(gymLevel)
        root.childById(ButtonComponent::class.java, "inc").onPress {
            this.incPress(1.0)
        }
        root.childById(ButtonComponent::class.java, "inc-ten").onPress {
            this.incPress(10.0)
        }
        root.childById(ButtonComponent::class.java, "dec").onPress {
            this.decPress(1.0)
        }
        root.childById(ButtonComponent::class.java, "dec-ten").onPress {
            this.decPress(10.0)
        }
        root.childById(ButtonComponent::class.java, "cancel").onPress {
            this.close()
        }
        root.childById(ButtonComponent::class.java, "start").onPress {
            this.sendStartGymPacket()
        }
    }

    private fun incPress(value: Double) {
        gymLevel += value;
        updateSlider()
        RadGyms.LOGGER.info("inc pressed")

    }

    private fun decPress(value: Double) {
        gymLevel -= value;
        updateSlider()
        RadGyms.LOGGER.info("dec pressed")
    }

    private fun updateSlider() {
        RadGyms.LOGGER.info("gym level: $gymLevel")
        root.childById(DiscreteSliderComponent::class.java, "gym_slider").setFromDiscreteValue(gymLevel)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun sendStartGymPacket() {
        try {
            var level: Int = this.gymLevel.toInt();
            if (level < 5) {
                level = 5
            }
            RadGyms.CHANNEL.clientHandle().send(NetworkStackProcessor.GymKeyPacketMessage(level))
        } catch (e: Exception) {
            RadGyms.LOGGER.info(e.toString())
        }
    }
}