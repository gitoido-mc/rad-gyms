/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.block;

import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantBlockMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    void radGyms$DimensionTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!serverLevel.isClientSide() && serverLevel.dimension() == RadGymsDimensions.GYM_DIMENSION) {
            ci.cancel();
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    void radGyms$DimensionCanSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (levelReader instanceof ServerLevel && ((ServerLevel) levelReader).dimension() == RadGymsDimensions.GYM_DIMENSION) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
