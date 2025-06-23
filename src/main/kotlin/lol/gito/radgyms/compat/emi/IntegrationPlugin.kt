package lol.gito.radgyms.compat.emi

import com.cobblemon.mod.common.CobblemonItems
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import lol.gito.radgyms.item.ItemRegistry
import net.minecraft.item.Items

object IntegrationPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        val gymKey = EmiStack.of(ItemRegistry.GYM_KEY).comparison(Comparison.compareComponents())
        registry.addEmiStackAfter(gymKey, EmiStack.of(Items.GOLD_INGOT))
        registry.addEmiStackAfter(gymKey, EmiStack.of(CobblemonItems.POKE_BALL))
    }
}
