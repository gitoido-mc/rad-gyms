package lol.gito.radgyms.mixin;

import kotlin.Suppress;
import lol.gito.radgyms.RadGyms;
import lol.gito.radgyms.nbt.EntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
@Suppress(names = "unused")
public abstract class DataSaver implements EntityDataSaver {
    @Shadow public abstract UUID getUuid();

    @Unique
    private NbtCompound persistentData;

    public @NotNull NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
            RadGyms.INSTANCE.debug("PersistentData created for player " + this.getUuid());
        }

        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void RadGyms$injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (persistentData != null) {
            nbt.put(RadGyms.MOD_ID + ".entity_data", persistentData);
            RadGyms.INSTANCE.debug("PersistentData wrote for player " + this.getUuid());
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void RadGyms$injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(RadGyms.MOD_ID + ".entity_data", NbtElement.COMPOUND_TYPE)) {
            persistentData = nbt.getCompound(RadGyms.MOD_ID + ".entity_data");
            RadGyms.INSTANCE.debug("PersistentData read for player " + this.getUuid());
        }
    }
}
