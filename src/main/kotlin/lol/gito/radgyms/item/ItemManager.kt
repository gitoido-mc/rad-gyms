package lol.gito.radgyms.item

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.MOD_ID

object ItemManager {
    fun register() {
        RadGyms.LOGGER.info("Registering items")
        FieldRegistrationHandler.register(ItemRegistry::class.java, MOD_ID, false)
    }
}