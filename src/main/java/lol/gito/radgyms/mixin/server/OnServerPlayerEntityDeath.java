/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.server;

import lol.gito.radgyms.api.enumeration.GymLeaveReason;
import lol.gito.radgyms.api.events.ModEvents;
import lol.gito.radgyms.common.gym.GymInstance;
import lol.gito.radgyms.server.state.RadGymsState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lol.gito.radgyms.common.registry.EventRegistry.GYM_LEAVE;

@Mixin(ServerPlayerEntity.class)
public abstract class OnServerPlayerEntityDeath {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void RadGyms$onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        GymInstance gym = RadGymsState.Companion.getGymForPlayer(player);
        if (gym != null) {
            GYM_LEAVE.emit(new ModEvents.GymLeaveEvent(
                    GymLeaveReason.PLAYER_DEATH,
                    player,
                    gym,
                    gym.getType(),
                    gym.getLevel(),
                    false,
                    false
            ));
        }
    }
}
