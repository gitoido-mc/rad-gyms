package lol.gito.radgyms.mixin;

import com.aetherteam.aether.event.listeners.EntityListener;
import lol.gito.radgyms.entity.Trainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityListener.class)
class AetherEntityInteract {
    @Inject(method = "onInteractWithEntity", at = @At("HEAD"), cancellable = true)
    private static void RadGyms$onEntityInteract(
        Entity targetEntity,
        PlayerEntity player,
        Hand interactionHand,
        EntityHitResult hitResult,
        CallbackInfoReturnable<ActionResult> cir
    ) {
        if (targetEntity instanceof Trainer) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
