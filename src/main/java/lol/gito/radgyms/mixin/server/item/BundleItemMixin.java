/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.mixin.server.item;

import lol.gito.radgyms.common.registry.RadGymsDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @Inject(method = "dropContents", at = @At("RETURN"), cancellable = true)
    private static void RadGyms$dropAllBundledItems(
        ItemStack itemStack,
        Player player,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = itemStack.get(RadGymsDataComponents.RG_GYM_BUNDLE_COMPONENT);

        if (!cir.getReturnValue()) {
            return;
        }

        if (bundleComponent != null && player instanceof ServerPlayer) {
            itemStack.shrink(1);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void RadGyms$preventOnStackClicked(
        ItemStack itemStack,
        Slot slot,
        ClickAction clickAction,
        Player player,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = itemStack.get(RadGymsDataComponents.RG_GYM_BUNDLE_COMPONENT);

        if (bundleComponent != null) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void RadGyms$preventOnClicked(
        ItemStack itemStack,
        ItemStack otherStack,
        Slot slot,
        ClickAction clickAction,
        Player player,
        SlotAccess slotAccess,
        CallbackInfoReturnable<Boolean> cir
    ) {
        Boolean bundleComponent = itemStack.get(RadGymsDataComponents.RG_GYM_BUNDLE_COMPONENT);
        Boolean bundleOtherComponent = otherStack.get(RadGymsDataComponents.RG_GYM_BUNDLE_COMPONENT);

        if (bundleComponent != null || bundleOtherComponent != null) {
            cir.setReturnValue(false);
        }
    }
}
