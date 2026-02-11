/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.worldBorder;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("resource")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyReceiver(
        method = "baseTick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;isWithinBounds(Lnet/minecraft/world/phys/AABB;)Z"
        )
    )
    private WorldBorder modifyContains(WorldBorder defaultBorder, AABB box) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (entity.level().dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) return RadGyms.dimensionWorldBorder;
        return defaultBorder;
    }

    @ModifyReceiver(
        method = "baseTick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;getDistanceToBorder(Lnet/minecraft/world/entity/Entity;)D"
        )
    )
    private WorldBorder modifyDistanceInsideBorder(WorldBorder defaultBorder, Entity entity) {
        if (entity.level().dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) return RadGyms.dimensionWorldBorder;
        return defaultBorder;
    }

    @ModifyReceiver(
        method = "baseTick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;getDamageSafeZone()D"
        )
    )
    private WorldBorder modifySafeZone(WorldBorder defaultBorder) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (entity.level().dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) return RadGyms.dimensionWorldBorder;
        return defaultBorder;
    }

    @ModifyReceiver(
        method = "baseTick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;getDamagePerBlock()D"
        )
    )
    private WorldBorder modifyDamagePerBlock(WorldBorder defaultBorder) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (entity.level().dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) return RadGyms.dimensionWorldBorder;
        return defaultBorder;
    }
}
