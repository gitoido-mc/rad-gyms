package lol.gito.radgyms.item

import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.gym.GymManager
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
                val components = ComponentMap.builder();
                components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, it.name)

                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }

            GymManager.GYM_TEMPLATES.filterNot { it.key == "default" }.map { it.key }.forEach {
                val components = ComponentMap.builder();
                components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, it)

                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }
        }
    }

    fun register() {
        LOGGER.info("Registering item groups")
        GYMS_GROUP.initialize()
    }
}