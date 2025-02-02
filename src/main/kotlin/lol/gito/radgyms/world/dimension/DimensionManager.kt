package lol.gito.radgyms.world.dimension

import lol.gito.radgyms.RadGyms
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World

object DimensionManager {
    val RADGYMS_LEVEL_KEY: RegistryKey<World> =
        RegistryKey.of(RegistryKeys.WORLD, RadGyms.modIdentifier("${RadGyms.MOD_ID}_dim"))

    fun register() {
        RadGyms.LOGGER.info("Registering RadGyms dimension")
    }
}