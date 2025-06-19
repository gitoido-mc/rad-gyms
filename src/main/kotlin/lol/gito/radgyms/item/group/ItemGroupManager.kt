package lol.gito.radgyms.item.group

import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.CommonPokeCache
import lol.gito.radgyms.item.GymKey
import lol.gito.radgyms.item.ItemRegistry
import lol.gito.radgyms.item.PokeCache
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Rarity

object ItemGroupManager {
    val GYMS_GROUP: OwoItemGroup = OwoItemGroup
        .builder(modId("items")) { Icon.of(ItemRegistry.GYM_KEY) }
        .disableDynamicTitle()
        .build()

    fun gymTypeItemStacks(item: Item, entries: ItemGroup.Entries) {
        if (item is GymKey) {
            entries.add(item.defaultStack)

            ElementalTypes.all().forEach {
                val components = ComponentMap.builder()
                components.add(DataComponentTypes.RARITY, Rarity.RARE)
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, it.name)

                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }

            GymManager.GYM_TEMPLATES
                .filterNot { it.key == "default" }
                .filterNot { it.key in ElementalTypes.all().map { it.name.lowercase() } }
                .map { it.key }
                .forEach {
                    val components = ComponentMap.builder()
                    components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                    components.add(DataComponentManager.GYM_TYPE_COMPONENT, it)

                    val stack = ItemStack(item)
                    stack.applyComponentsFrom(components.build())
                    entries.add(stack)
                }
        }
    }

    fun cacheItemStacks(item: Item, entries: ItemGroup.Entries) {
        if (item is PokeCache) {
            val stack = ItemStack(item)
            entries.add(stack)
            ElementalTypes.all().forEach {
                val components = ComponentMap.builder()
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, it.name.lowercase())
                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }
        }
    }

    fun register() {
        debug("Registering item groups")
        GYMS_GROUP.initialize()
    }
}
