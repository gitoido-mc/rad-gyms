/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.mixin.server;

import lol.gito.radgyms.common.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @Inject(method = "dropAllBundledItems", at = @At("RETURN"), cancellable = true)
    private static void RadGyms$dropAllBundledItems(
            ItemStack stack,
            PlayerEntity player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = stack.get(DataComponentRegistry.INSTANCE.getRAD_GYM_BUNDLE_COMPONENT());

        if (!cir.getReturnValue()) {
            return;
        }

        if (bundleComponent != null && player instanceof ServerPlayerEntity) {
            stack.decrement(1);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onStackClicked", at = @At("HEAD"), cancellable = true)
    private void RadGyms$preventOnStackClicked(
            ItemStack stack,
            Slot slot,
            ClickType clickType,
            PlayerEntity player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = stack.get(DataComponentRegistry.INSTANCE.getRAD_GYM_BUNDLE_COMPONENT());

        if (bundleComponent != null) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    private void RadGyms$preventOnClicked(
            ItemStack stack,
            ItemStack otherStack,
            Slot slot,
            ClickType clickType,
            PlayerEntity player,
            StackReference cursorStackReference,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = stack.get(DataComponentRegistry.INSTANCE.getRAD_GYM_BUNDLE_COMPONENT());
        Boolean bundleOtherComponent = otherStack.get(DataComponentRegistry.INSTANCE.getRAD_GYM_BUNDLE_COMPONENT());

        if (bundleComponent != null || bundleOtherComponent != null) {
            cir.setReturnValue(false);
        }
    }
}
