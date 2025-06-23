package lol.gito.radgyms.item.renderer

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.Block
import net.minecraft.block.StainedGlassPaneBlock
import net.minecraft.block.TranslucentBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

abstract class SpecialItemRenderer {

    abstract fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        leftHanded: Boolean,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    )

    fun renderModel(
        id: ModelIdentifier,
        stack: ItemStack,
        mode: ModelTransformationMode,
        leftHanded: Boolean,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        renderModel(id, stack, mode, leftHanded, matrices, vertexConsumers, light, overlay) {}
    }

    fun renderModel(
        id: ModelIdentifier,
        stack: ItemStack,
        mode: ModelTransformationMode,
        leftHanded: Boolean,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
        action: () -> Unit
    ) {
        matrices.push()
        val model = MinecraftClient.getInstance().bakedModelManager.getModel(id)

        if (mode == ModelTransformationMode.GUI && !model.isSideLit) {
            DiffuseLighting.disableGuiDepthLighting()
        }

        matrices.translate(0.5f, 0.5f, 0.5f)
        model.transformation.getTransformation(mode).apply(leftHanded, matrices)
        matrices.translate(-0.5f, -0.5f, -0.5f)

        action()

        val opaque = if (mode != ModelTransformationMode.GUI && !mode.isFirstPerson && stack.item is BlockItem) {
            val block: Block = (stack.item as BlockItem).block
            block !is TranslucentBlock && block !is StainedGlassPaneBlock
        } else {
            true
        }

        val renderLayer = RenderLayers.getItemLayer(stack, opaque)
        val vertexConsumer = if (opaque) {
            ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint())
        } else {
            ItemRenderer.getItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint())
        }   

        renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer)

        if (mode == ModelTransformationMode.GUI && !model.isSideLit) {
            if (vertexConsumers is VertexConsumerProvider.Immediate) {
                RenderSystem.disableDepthTest()
                vertexConsumers.draw()
                RenderSystem.enableDepthTest()
            }

            DiffuseLighting.enableGuiDepthLighting()
        }

        matrices.pop()
    }

    private fun renderBakedItemModel(
        model: BakedModel,
        stack: ItemStack,
        light: Int,
        overlay: Int,
        matrices: MatrixStack,
        vertices: VertexConsumer
    ) {
        val random = Random.create()

        for (direction in Direction.entries) {
            random.setSeed(42L)
            renderBakedItemQuads(matrices, vertices, model.getQuads(null, direction, random), stack, light, overlay)
        }

        random.setSeed(42L)
        renderBakedItemQuads(matrices, vertices, model.getQuads(null, null, random), stack, light, overlay)
    }

    private fun renderBakedItemQuads(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        quads: List<BakedQuad>,
        stack: ItemStack,
        light: Int,
        overlay: Int
    ) {
        val entry = matrices.peek()

        for (bakedQuad in quads) {
            var color = -1

            if (!stack.isEmpty && bakedQuad.hasColor()) {
                color = MinecraftClient.getInstance().itemColors.getColor(stack, bakedQuad.colorIndex)
            }

            val alpha = ColorHelper.Argb.getAlpha(color).toFloat() / 255.0f
            val red = ColorHelper.Argb.getRed(color).toFloat() / 255.0f
            val green = ColorHelper.Argb.getGreen(color).toFloat() / 255.0f
            val blue = ColorHelper.Argb.getBlue(color).toFloat() / 255.0f

            vertices.quad(entry, bakedQuad, red, green, blue, alpha, light, overlay)
        }
    }

}