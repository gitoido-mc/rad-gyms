package lol.gito.radgyms.item

import io.wispforest.owo.registration.reflect.ItemRegistryContainer
import net.minecraft.item.Item

class ItemRegistry : ItemRegistryContainer {
    companion object {
        @JvmField
        val GYM_KEY = GymKey(Item.Settings().group(ItemGroupManager.GYMS_GROUP))
    }
}