/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.worldBorder;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapOperation(
        method = "collectColliders(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"
        )
    )
    private static WorldBorder sendModifiedBorder(Level instance, Operation<WorldBorder> original, Entity entity, Level level) {
        if (level.dimension() == RadGymsDimensions.GYM_DIMENSION) return RadGyms.dimensionWorldBorder;
        return original.call(level);
    }
}
