/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.worldBorder;


import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import lol.gito.radgyms.common.RadGyms;
import lol.gito.radgyms.common.registry.RadGymsDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @ModifyReceiver(
        method = "mayInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/border/WorldBorder;isWithinBounds(Lnet/minecraft/core/BlockPos;)Z"
        )
    )
    private WorldBorder RadGyms$modifyContains(WorldBorder defaultBorder, BlockPos pos, @Local(argsOnly = true) Player player) {
        if (!(player instanceof ServerPlayer)) return defaultBorder;
        if (player.level().dimension() != RadGymsDimensions.RADGYMS_LEVEL_KEY) return defaultBorder;
        return RadGyms.dimensionWorldBorder;
    }

}
