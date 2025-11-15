/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.client;

import lol.gito.radgyms.RadGyms;
import lol.gito.radgyms.client.RadGymsClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class OnLoadModels {
    @Shadow
    @Final
    private Map<Identifier, JsonUnbakedModel> jsonUnbakedModels;

    @Shadow
    protected abstract void loadItemModel(ModelIdentifier id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.AFTER))
    public void init(
        BlockColors blockColors,
        Profiler profiler,
        Map<Identifier, JsonUnbakedModel> jsonUnbakedModels,
        Map<Identifier, List<ModelLoader.SpriteGetter>> blockStates,
        CallbackInfo ci
    ) {
        this.jsonUnbakedModels.keySet().stream()
            .filter(id -> {
                String path = id.getPath();
                boolean check = path.startsWith("models/item/gym_key") && path.endsWith(".json");
                if (check) {
                    RadGyms.INSTANCE.getLOGGER().info(path);
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
            .forEach(id -> Arrays.stream(ModelTransformationMode.values())
                .forEach(item ->
                    this.loadItemModel(RadGymsClient.INSTANCE.modModelId(id, item.asString()))
                )
            );
    }
}
