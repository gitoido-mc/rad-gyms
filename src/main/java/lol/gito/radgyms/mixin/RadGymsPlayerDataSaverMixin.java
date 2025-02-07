package lol.gito.radgyms.mixin;

import kotlin.Suppress;
import lol.gito.radgyms.RadGyms;
import lol.gito.radgyms.nbt.EntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@Suppress(names = "unused")
public abstract class RadGymsPlayerDataSaverMixin implements EntityDataSaver {
    @Unique
    private NbtCompound persistentData;

    @SuppressWarnings("NullableProblems")
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }

        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (persistentData != null) {
            nbt.put(RadGyms.MOD_ID + ".entity_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(RadGyms.MOD_ID + ".entity_data", NbtElement.COMPOUND_TYPE)) {
            persistentData = nbt.getCompound(RadGyms.MOD_ID + ".entity_data");
        }
    }
}
