package lol.gito.radgyms.mixin;

import lol.gito.radgyms.gym.GymManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class OnServerPlayerDeath {
    @Inject(method = "onDeath", at = @At("RETURN"), remap = false)
    public void RadGyms$onDeath(DamageSource damageSource, CallbackInfo ci) {
        GymManager.INSTANCE.destructGym((ServerPlayerEntity) (Object) this);
    }
}
