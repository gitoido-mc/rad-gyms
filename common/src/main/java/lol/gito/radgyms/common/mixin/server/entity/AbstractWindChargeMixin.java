/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.entity;

import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWindCharge.class)
public class AbstractWindChargeMixin {
    @Inject(method = "onHitBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"), cancellable = true)
    private void radGyms$onHitBlock(BlockHitResult blockHitResult, CallbackInfo ci) {
        AbstractWindCharge entity = (AbstractWindCharge) (Object) this;
        if (entity.level() instanceof ServerLevel && entity.level().dimension() == RadGymsDimensions.GYM_DIMENSION) ci.cancel();
    }

    @Inject(
            method = "onHitEntity",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/EntityHitResult;getEntity()Lnet/minecraft/world/entity/Entity;",
                    ordinal = 0
            )
    )
    private void radGyms$onHitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        AbstractWindCharge entity = (AbstractWindCharge) (Object) this;
        if (entity.level() instanceof ServerLevel && entity.level().dimension() == RadGymsDimensions.GYM_DIMENSION) ci.cancel();
    }
}
