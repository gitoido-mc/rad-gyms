package lol.gito.radgyms.block

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.MOD_ID
import lol.gito.radgyms.block.entity.BlockEntityRegistry

object BlockManager {
    fun register() {
        RadGyms.LOGGER.info("Registering blocks and block entities")
        BlockEntityRegistry.register()
        FieldRegistrationHandler.register(BlockRegistry::class.java, MOD_ID, false)
    }
}