/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.worldBorder;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("resource")
@Mixin(CollisionGetter.class)
public interface CollisionGetterMixin {
    @WrapOperation(
        method = "borderCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/CollisionGetter;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"
        )
    )
    private WorldBorder sendModifiedBorder(CollisionGetter instance, Operation<WorldBorder> original, @Local(argsOnly = true) Entity entity) {
        if (entity.level().dimension() == RadGymsDimensions.GYM_DIMENSION) return RadGyms.dimensionWorldBorder;
        return original.call(instance);
    }

    @SuppressWarnings("rawtypes")
    @WrapOperation(
        method = "findFreePosition(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/Vec3;DDD)Ljava/util/Optional;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
        )
    )
    private Stream sendModifiedBorder(Stream instance, Predicate predicate, Operation<Stream> original, @Local(argsOnly = true) Entity entity) {
        if (entity.level().dimension() == RadGymsDimensions.GYM_DIMENSION) {
            Predicate newPredicate = voxelShape -> RadGyms
                .dimensionWorldBorder
                .isWithinBounds(((VoxelShape)voxelShape).bounds());
            return original.call(instance, newPredicate);
        }
        return original.call(instance, predicate);
    }
}
