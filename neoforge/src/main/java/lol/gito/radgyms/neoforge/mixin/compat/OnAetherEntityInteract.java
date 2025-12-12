/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.neoforge.mixin.compat;

import com.aetherteam.aether.event.listeners.EntityListener;
import lol.gito.radgyms.common.entity.Trainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityListener.class)
class OnAetherEntityInteract {
    @Inject(method = "onInteractWithEntity", at = @At("HEAD"), cancellable = true, remap = false)
    private static void RadGyms$onEntityInteract(
            PlayerInteractEvent.EntityInteractSpecific event, CallbackInfo ci
    ) {
        if (event.getTarget() instanceof Trainer) {
            ci.cancel();
        }
    }
}
