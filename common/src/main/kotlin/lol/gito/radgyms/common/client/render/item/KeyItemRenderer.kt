package lol.gito.radgyms.common.client.render.item

import com.mojang.blaze3d.vertex.PoseStack
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.client.RadGymsClient.modModelId
import lol.gito.radgyms.common.registry.RadGymsDataComponents
import lol.gito.radgyms.common.registry.RadGymsItems
import net.minecraft.client.Minecraft
import net.minecraft.client.color.item.ItemColors
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.ModelManager
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class KeyItemRenderer(
    minecraft: Minecraft,
    textureManager: TextureManager,
    val modelManager: ModelManager,
    itemColors: ItemColors,
    blockEntityWithoutLevelRenderer: BlockEntityWithoutLevelRenderer,
) : ItemRenderer(minecraft, textureManager, modelManager, itemColors, blockEntityWithoutLevelRenderer) {
    override fun render(
        itemStack: ItemStack,
        itemDisplayContext: ItemDisplayContext,
        bl: Boolean,
        poseStack: PoseStack,
        multiBufferSource: MultiBufferSource,
        i: Int,
        j: Int,
        bakedModel: BakedModel,
    ) {
        if (!itemStack.`is`(RadGymsItems.GYM_KEY)) {
            super.render(itemStack, itemDisplayContext, bl, poseStack, multiBufferSource, i, j, bakedModel)
            return
        }

        val type = itemStack.components.get(RadGymsDataComponents.RG_GYM_TYPE_COMPONENT)
        if (type == null) {
            super.render(itemStack, itemDisplayContext, bl, poseStack, multiBufferSource, i, j, bakedModel)
            return
        }

        val model = modelManager.getModel(modModelId(modId("gym_key_$type"), itemDisplayContext.serializedName))
        if (model != modelManager.missingModel) {
            super.render(itemStack, itemDisplayContext, bl, poseStack, multiBufferSource, i, j, model)
            return
        }

        super.render(itemStack, itemDisplayContext, bl, poseStack, multiBufferSource, i, j, bakedModel)
    }
}
