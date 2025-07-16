package lol.gito.radgyms.item.renderer

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack

class GymKeyRenderer : SpecialItemRenderer() {
    companion object {
        val INSTANCE = GymKeyRenderer()
    }

    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        leftHanded: Boolean,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        var model = RadGyms.modModelId("gym_key_default", "inventory")
        var type = stack.get(DataComponentManager.GYM_TYPE_COMPONENT)
        var overrideModel = RadGyms.modModelId("gym_keys/$type", "inventory")
        var modelManager = MinecraftClient.getInstance().bakedModelManager

        if (modelManager.getModel(overrideModel) != modelManager.missingModel) {
            model = overrideModel
        }

        renderModel(model, stack, mode, leftHanded, matrices, vertexConsumers, light, overlay)
    }
}