package lol.gito.radgyms.mixin;

import lol.gito.radgyms.gym.GymManager;
import lol.gito.radgyms.world.DimensionManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class OnServerPlayerDeath {
    @Inject(method = "onDeath", at = @At("TAIL"))
    public void RadGyms$onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (player.getWorld().getRegistryKey() == DimensionManager.INSTANCE.getRADGYMS_LEVEL_KEY()) {
            GymManager.INSTANCE.destructGym(player);
        }
    }
}
