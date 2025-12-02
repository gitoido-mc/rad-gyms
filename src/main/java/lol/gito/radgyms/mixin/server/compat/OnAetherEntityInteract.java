/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.server.compat;

import com.aetherteam.aether.event.listeners.EntityListener;
import lol.gito.radgyms.common.entity.Trainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityListener.class)
class OnAetherEntityInteract {
    @Inject(method = "onInteractWithEntity", at = @At("HEAD"), cancellable = true)
    private static void RadGyms$onEntityInteract(
            Entity targetEntity,
            Player player,
            InteractionHand interactionHand,
            EntityHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (targetEntity instanceof Trainer) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
