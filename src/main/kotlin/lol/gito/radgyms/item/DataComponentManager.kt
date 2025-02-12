package lol.gito.radgyms.item

import com.mojang.serialization.Codec
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.modIdentifier
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object DataComponentManager {
    val GYM_TYPE_COMPONENT: ComponentType<String> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        modIdentifier("gym_type_component"),
        ComponentType.builder<String>().codec(Codec.STRING).build()
    )

    fun register() {
        RadGyms.LOGGER.info("Registering data components")
    }
}