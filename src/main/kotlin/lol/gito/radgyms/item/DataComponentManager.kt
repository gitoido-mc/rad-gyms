package lol.gito.radgyms.item

import com.mojang.serialization.Codec
import lol.gito.radgyms.RadGyms.LOGGER
import lol.gito.radgyms.RadGyms.modId
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries.DATA_COMPONENT_TYPE
import net.minecraft.registry.Registry

object DataComponentManager {
    val GYM_TYPE_COMPONENT: ComponentType<String> = Registry.register(
        DATA_COMPONENT_TYPE,
        modId("gym_type_component"),
        ComponentType.builder<String>().codec(Codec.STRING).build()
    )

    fun register() {
        LOGGER.info("Registering data components")
    }
}