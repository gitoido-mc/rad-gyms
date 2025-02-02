package lol.gito.radgyms.block

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler
import lol.gito.radgyms.RadGyms.MOD_ID

object BlockManager {
    fun register() {
        FieldRegistrationHandler.register(BlockRegistry::class.java, MOD_ID, false)
    }
}