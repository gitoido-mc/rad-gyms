package lol.gito.radgyms.compat.jei

import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.item.ItemRegistry
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import mezz.jei.api.registration.IModInfoRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

@JeiPlugin
class IntegrationPlugin : IModPlugin {
    override fun getPluginUid(): Identifier = RadGyms.modId("jei-plugin")

    override fun registerModInfo(modAliasRegistration: IModInfoRegistration) {
        modAliasRegistration.addModAliases(RadGyms.MOD_ID, "rg", "crg")
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(ItemRegistry.GYM_KEY, GymKeyAttuneInterpreter)
        registration.registerSubtypeInterpreter(ItemRegistry.CACHE_COMMON, CacheAttuneInterpreter)
        registration.registerSubtypeInterpreter(ItemRegistry.CACHE_UNCOMMON, CacheAttuneInterpreter)
        registration.registerSubtypeInterpreter(ItemRegistry.CACHE_RARE, CacheAttuneInterpreter)
        registration.registerSubtypeInterpreter(ItemRegistry.CACHE_EPIC, CacheAttuneInterpreter)
    }
}

object GymKeyAttuneInterpreter : ISubtypeInterpreter<ItemStack> {
    override fun getSubtypeData(
        ingredient: ItemStack,
        context: UidContext
    ): Any {
        val type = ingredient.components.getOrDefault(DataComponentManager.GYM_TYPE_COMPONENT, "chaos")
        return context.name + "_" + type
    }

    @Deprecated("Deprecated in Java")
    override fun getLegacyStringSubtypeInfo(
        ingredient: ItemStack,
        context: UidContext
    ): String {
        val type = ingredient.components.getOrDefault(DataComponentManager.GYM_TYPE_COMPONENT, "chaos")
        return ingredient.item.toString() + "_" + type
    }
}

object CacheAttuneInterpreter : ISubtypeInterpreter<ItemStack> {
    override fun getSubtypeData(
        ingredient: ItemStack,
        context: UidContext
    ): Any {
        val type = ingredient.components.getOrDefault(DataComponentManager.GYM_TYPE_COMPONENT, "chaos")
        val shinyBoost = ingredient.components.getOrDefault(DataComponentManager.CACHE_SHINY_BOOST_COMPONENT, null)
        if (shinyBoost != null) {
            return context.name + "_" + type + "_boosted"
        }
        return context.name + "_" + type
    }

    @Deprecated("Deprecated in Java")
    override fun getLegacyStringSubtypeInfo(
        ingredient: ItemStack,
        context: UidContext
    ): String {
        val type = ingredient.components.getOrDefault(DataComponentManager.GYM_TYPE_COMPONENT, "chaos")
        val shinyBoost = ingredient.components.getOrDefault(DataComponentManager.CACHE_SHINY_BOOST_COMPONENT, null)
        if (shinyBoost != null) {
            return ingredient.item.toString() + "_" + type + "_boosted"
        }

        return ingredient.item.toString() + "_" + type
    }
}
