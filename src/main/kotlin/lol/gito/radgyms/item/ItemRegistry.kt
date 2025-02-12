package lol.gito.radgyms.item

import io.wispforest.owo.registration.reflect.ItemRegistryContainer

class ItemRegistry : ItemRegistryContainer {
    companion object {
        @JvmField
        val GYM_KEY = GymKey()
    }
}