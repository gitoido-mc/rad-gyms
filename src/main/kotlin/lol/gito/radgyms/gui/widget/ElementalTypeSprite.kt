package lol.gito.radgyms.gui.widget

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.client.gui.TypeIcon
import com.mojang.blaze3d.systems.RenderSystem
import io.wispforest.owo.ui.component.TextureComponent
import io.wispforest.owo.ui.core.OwoUIDrawContext
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

class ElementalTypeSprite(
    private val type: ElementalType,
    u: Int,
    v: Int,
    regionWidth: Int,
    regionHeight: Int,
    textureWidth: Int,
    textureHeight: Int
) : TextureComponent(
    Identifier.ofVanilla("air"),
    u,
    v,
    regionWidth,
    regionHeight,
    textureWidth,
    textureHeight
) {
    override fun draw(context: OwoUIDrawContext, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
        RenderSystem.enableDepthTest()

        if (this.blend) {
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
        }

        val matrices = context.matrices
        matrices.push()
        matrices.translate(x.toFloat(), y.toFloat(), 0f)
        matrices.scale(this.width / regionWidth.toFloat(), this.height / regionHeight.toFloat(), 0f)

        TypeIcon(
            x = (this.width / textureWidth) / 2,
            y = (this.height / textureHeight) / 2,
            type = this.type
        ).render(context as DrawContext)

        if (this.blend) {
            RenderSystem.disableBlend()
        }

        matrices.pop()
    }
}
