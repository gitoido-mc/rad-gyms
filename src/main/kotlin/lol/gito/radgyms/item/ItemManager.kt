package lol.gito.radgyms.item

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler
import lol.gito.radgyms.RadGyms.MOD_ID
import lol.gito.radgyms.RadGyms.debug

object ItemManager {
    fun register() {
        debug("Registering items")
        FieldRegistrationHandler.register(ItemRegistry::class.java, MOD_ID, false)
    }
}
