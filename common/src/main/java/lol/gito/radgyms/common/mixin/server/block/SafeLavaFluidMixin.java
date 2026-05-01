/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.block;

import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFluid.class)
public abstract class SafeLavaFluidMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void radGyms$SafeLavaInDimension(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource, CallbackInfo ci) {
        if (level.dimension() == RadGymsDimensions.GYM_DIMENSION) ci.cancel();
    }
}
