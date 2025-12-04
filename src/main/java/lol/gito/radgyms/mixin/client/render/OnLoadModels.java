/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.mixin.client.render;

import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.fabric.client.RadGymsFabricClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class OnLoadModels {
    @Shadow
    @Final
    private Map<ResourceLocation, BlockModel> modelResources;

    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation id);

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    public void init(
        BlockColors blockColors,
        ProfilerFiller profiler,
        Map<ResourceLocation, BlockModel> jsonUnbakedModels,
        Map<ResourceLocation, List<ModelBakery.TextureGetter>> blockStates,
        CallbackInfo ci
    ) {
        this.modelResources.keySet().stream()
            .filter(id -> {
                String path = id.getPath();
                boolean check = path.startsWith("models/item/gym_key") && path.endsWith(".json");
                if (check) {
                    RadGyms.LOGGER.info(path);
                }
                return check;
            })
            .map(id -> id
                .withPath(
                    id.getPath()
                        .substring(
                            "models/item/".length(),
                            id.getPath().length() - ".json".length()
                        )
                )
            )
            .forEach(id -> Arrays.stream(ItemDisplayContext.values())
                .forEach(item ->
                    this.loadSpecialItemModelAndDependencies(RadGymsFabricClient.INSTANCE.modModelId(id, item.getSerializedName()))
                )
            );
    }
}
