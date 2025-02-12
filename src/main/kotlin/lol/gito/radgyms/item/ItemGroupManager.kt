package lol.gito.radgyms.item

import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.gym.GymManager
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Rarity


object ItemGroupManager {
    val GYMS_GROUP: OwoItemGroup = OwoItemGroup
        .builder(modIdentifier("items")) { Icon.of(ItemRegistry.GYM_KEY) }
        .disableDynamicTitle()
        .build()

    fun gymTypeItemStacks(item: Item, entries: ItemGroup.Entries) {
        if (item is GymKey) {
            entries.add(item.defaultStack)

            for (type in ElementalTypes.all()) {
                val components = ComponentMap.builder();
                components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, type.name)

                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }

            for (type in GymManager.GYM_TEMPLATES.filterNot { it.key == "default" }.map { it.key }) {
                val components = ComponentMap.builder();
                components.add(DataComponentTypes.RARITY, Rarity.EPIC)
                components.add(DataComponentManager.GYM_TYPE_COMPONENT, type)

                val stack = ItemStack(item)
                stack.applyComponentsFrom(components.build())
                entries.add(stack)
            }
        }
    }

    fun register() {
        RadGyms.LOGGER.info("Registering item groups")
        GYMS_GROUP.initialize()
    }
}