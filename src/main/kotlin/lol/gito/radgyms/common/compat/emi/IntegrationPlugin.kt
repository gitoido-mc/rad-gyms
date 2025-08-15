/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.compat.emi

import com.cobblemon.mod.common.CobblemonItems
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import lol.gito.radgyms.common.registry.ItemRegistry
import net.minecraft.item.Items

object IntegrationPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        val gymKey = EmiStack.of(ItemRegistry.GYM_KEY).comparison(Comparison.compareComponents())
        registry.addEmiStackAfter(gymKey, EmiStack.of(Items.GOLD_INGOT))
        registry.addEmiStackAfter(gymKey, EmiStack.of(CobblemonItems.POKE_BALL))
    }
}
