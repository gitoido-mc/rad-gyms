package lol.gito.radgyms.gui

import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.DiscreteSliderComponent
import io.wispforest.owo.ui.component.LabelComponent
import io.wispforest.owo.ui.container.FlowLayout
import lol.gito.radgyms.RadGyms.CHANNEL
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gui.GymGUIIdentifiers.ID_GYM_SLIDER
import lol.gito.radgyms.gui.GymGUIIdentifiers.ID_USAGE
import lol.gito.radgyms.network.NetworkStackHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text.translatable
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import org.lwjgl.glfw.GLFW

class GymEntranceScreen(
    override val type: String? = null,
    override val id: Identifier = GymGUIIdentifiers.UI_GYM_ENTRANCE,
    override val player: PlayerEntity,
    private val blockPos: BlockPos,
    private val usesLeft: Int
) :
    GymEnterScreen(type, id, player),
    IGymEnterScreen
{
    override fun build(root: FlowLayout) {
        this.root = root
        root.childById(LabelComponent::class.java, ID_USAGE).text(
            translatable(modId("gui.common.uses_left").toTranslationKey(), usesLeft)
        )
        super.build(root)
    }

    override fun sendStartGymPacket() {
        try {
            val level: Int = this.gymLevel.toInt().coerceIn(5, 100)

            val chosenType = when (type) {
                null -> ElementalTypes.all().random().name
                else -> type
            }

            this.close()

            debug("Sending GymEnterWithCoords(level:$level, type:$type, blockPos:$blockPos) C2S packet from ${player.name}")

            CHANNEL.clientHandle().send(
                NetworkStackHandler.GymEnterWithCoords(
                    level = level,
                    type = chosenType,
                    blockPos = blockPos.asLong()
                )
            )
        } catch (e: Exception) {
            LOGGER.warn(e.toString())
        }
    }
}
