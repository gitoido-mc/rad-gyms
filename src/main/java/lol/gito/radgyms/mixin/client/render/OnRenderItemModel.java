/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.common.registry.RadGymsDataComponents;
import lol.gito.radgyms.common.registry.RadGymsItems;
import lol.gito.radgyms.fabric.client.RadGymsFabricClient;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class OnRenderItemModel {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Unique
    @Final
    final private DataComponentType<String> RadGyms$typeComponent = RadGymsDataComponents.RG_GYM_TYPE_COMPONENT;

    @Inject(
        method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
            shift = At.Shift.AFTER
        )
    )
    private void RadGyms$overrideKeyModel(
        ItemStack stack,
        ItemDisplayContext renderMode,
        boolean leftHanded,
        PoseStack matrices,
        MultiBufferSource vertexConsumers,
        int light,
        int overlay,
        BakedModel model,
        CallbackInfo ci,
        @Local(argsOnly = true) LocalRef<BakedModel> override
    ) {
        if (stack.is(RadGymsItems.GYM_KEY) && stack.get(RadGyms$typeComponent) != null) {
            String type = stack.get(RadGyms$typeComponent);
            if (type == null) return;

            ResourceLocation intermediate = RadGyms.INSTANCE.modId("gym_key_" + type);
            ModelManager manager = this.itemModelShaper.getModelManager();
            ModelResourceLocation id = RadGymsFabricClient.INSTANCE.modModelId(intermediate, renderMode.getSerializedName());

            if (!manager.getModel(id).equals(manager.getMissingModel())) {
                override.set(manager.getModel(id));
            }
        }
    }
}

