/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin;

import com.mojang.authlib.GameProfile;
import lol.gito.radgyms.RadGyms;
import lol.gito.radgyms.nbt.EntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class OnServerPlayerCopy extends PlayerEntity {
    public OnServerPlayerCopy(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    protected void RadGyms$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ((EntityDataSaver)this).setGymsPersistentData(((EntityDataSaver)oldPlayer).getGymsPersistentData());
        RadGyms.INSTANCE.debug("PersistentData copied for player " + this.getUuid());
    }
}
