package lol.gito.radgyms.client.renderer

import com.cobblemon.mod.common.client.render.models.blockbench.repository.BlockEntityModelRepository
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.state.property.Properties.HORIZONTAL_FACING
import net.minecraft.util.math.RotationAxis

class GymEntranceEntityRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<GymEntranceEntity> {
    val context = RenderContext().also {
        it.put(RenderContext.RENDER_STATE, RenderContext.RenderState.BLOCK)
        it.put(RenderContext.DO_QUIRKS, true)
    }

    override fun render(
        entity: GymEntranceEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {

        val aspects = emptySet<String>()
        val state = entity.posableState
        state.currentAspects = aspects
        state.updatePartialTicks(tickDelta)

        val poserId = entity.type

        val model = BlockEntityModelRepository.getPoser(poserId, state)
        model.context = context
        val texture = BlockEntityModelRepository.getTexture(poserId, state)
        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture))
        model.bufferProvider = vertexConsumers
        state.currentModel = model
        context.put(RenderContext.ASPECTS, aspects)
        context.put(RenderContext.TEXTURE, texture)
        context.put(RenderContext.SPECIES, poserId)
        context.put(RenderContext.POSABLE_STATE, state)

        matrices.push()
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f))
        matrices.translate(-0.5, 0.0, 0.5)
        matrices.multiply(
            RotationAxis.POSITIVE_Y.rotationDegrees(
                entity.cachedState.get(HORIZONTAL_FACING).asRotation()
            )
        )
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f))

        model.applyAnimations(
            entity = null,
            state = state,
            headYaw = 0F,
            headPitch = 0F,
            limbSwing = 0F,
            limbSwingAmount = 0F,
            ageInTicks = state.animationSeconds * 20
        )
        model.render(context, matrices, vertexConsumer, light, overlay, -0x1)
        model.withLayerContext(vertexConsumers, state, BlockEntityModelRepository.getLayers(poserId, state)) {
            model.render(context, matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -0x1)
        }
        model.setDefault()
        matrices.pop()
    }
}
