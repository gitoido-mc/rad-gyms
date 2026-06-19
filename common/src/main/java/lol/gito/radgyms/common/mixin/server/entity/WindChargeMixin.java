/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.entity;

import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WindCharge.class)
public class WindChargeMixin {
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void radGyms$explode(Vec3 vec3, CallbackInfo ci) {
        AbstractWindCharge entity = (AbstractWindCharge) (Object) this;
        if (entity.level().dimension() == RadGymsDimensions.GYM_DIMENSION) ci.cancel();
    }
}
