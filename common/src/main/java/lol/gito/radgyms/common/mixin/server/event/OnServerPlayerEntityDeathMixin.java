/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.mixin.server.event;

import lol.gito.radgyms.common.api.dto.gym.Gym;
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason;
import lol.gito.radgyms.common.api.event.GymEvents;
import lol.gito.radgyms.common.world.state.RadGymsState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE;

@Mixin(ServerPlayer.class)
public abstract class OnServerPlayerEntityDeathMixin {
    @Inject(method = "die", at = @At("HEAD"))
    public void RadGyms$onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        Gym gym = RadGymsState.Companion.getGymForPlayer(player);
        if (gym != null) {
            GYM_LEAVE.emit(new GymEvents.GymLeaveEvent(
                GymLeaveReason.PLAYER_DEATH,
                player,
                false,
                false,
                gym
            ));
        }
    }
}
