package lol.gito.radgyms.item.dataComponent

import com.mojang.serialization.Codec
import lol.gito.radgyms.RadGyms.debug
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

    val RAD_GYM_BUNDLE_COMPONENT: ComponentType<Boolean> = Registry.register(
        DATA_COMPONENT_TYPE,
        modId("bundle"),
        ComponentType.builder<Boolean>().codec(Codec.BOOL).build()
    )

    val CACHE_SHINY_BOOST_COMPONENT: ComponentType<Int> = Registry.register(
        DATA_COMPONENT_TYPE,
        modId("shiny_boost"),
        ComponentType.builder<Int>().codec(Codec.INT).build()
    )

    fun register() {
        debug("Registering data components")
    }
}
