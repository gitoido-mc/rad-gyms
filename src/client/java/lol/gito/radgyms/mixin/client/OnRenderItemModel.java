/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lol.gito.radgyms.RadGyms;
import lol.gito.radgyms.client.RadGymsClient;
import lol.gito.radgyms.common.registry.DataComponentRegistry;
import lol.gito.radgyms.common.registry.ItemRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemRenderer.class)
public abstract class OnRenderItemModel {
    @Shadow
    @Final
    private ItemModels models;

    @Inject(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", shift = At.Shift.AFTER)
    )
    private void RadGyms$overrideKeyModel(
        ItemStack stack,
        ModelTransformationMode renderMode,
        boolean leftHanded,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        int overlay,
        BakedModel model,
        CallbackInfo ci,
        @Local(argsOnly = true) LocalRef<BakedModel> override
    ) {
        if (stack.isOf(ItemRegistry.INSTANCE.getGYM_KEY())
            && stack.get(DataComponentRegistry.INSTANCE.getGYM_TYPE_COMPONENT()) != null
            && !Objects.equals(stack.get(DataComponentRegistry.INSTANCE.getGYM_TYPE_COMPONENT()), "chaos")
        ) {
            String type = stack.get(DataComponentRegistry.INSTANCE.getGYM_TYPE_COMPONENT());
            if (type == null) return;

            Identifier intermediate = RadGyms.INSTANCE.modId("gym_key_" + type.toLowerCase());
            BakedModelManager manager = this.models.getModelManager();

            if (!manager
                .getModel(RadGymsClient.INSTANCE.modModelId(intermediate, renderMode.asString()))
                .equals(manager.getMissingModel())
            ) {
                override.set(manager
                    .getModel(RadGymsClient.INSTANCE.modModelId(intermediate, renderMode.asString()))
                );
            }
        }
    }
}

