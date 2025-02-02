package lol.gito.radgyms

import com.gitlab.srcmc.rctapi.api.RCTApi
import io.wispforest.owo.network.OwoNetChannel
import lol.gito.radgyms.block.BlockManager
import lol.gito.radgyms.command.CommandManager
import lol.gito.radgyms.gym.GymLoader
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemGroupManager
import lol.gito.radgyms.item.ItemManager
import lol.gito.radgyms.network.NetworkStackProcessor
import lol.gito.radgyms.pokemon.SpeciesManager
import lol.gito.radgyms.rct.RCTEventManager
import lol.gito.radgyms.rct.RCTCommandManager
import lol.gito.radgyms.world.dimension.DimensionManager
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RadGyms : ModInitializer {
    const val MOD_ID: String = "rad-gyms"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val CHANNEL: OwoNetChannel = OwoNetChannel.create(modIdentifier("main"))
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)
    val GYM_LOADER: GymLoader = GymLoader()


    override fun onInitialize() {
        LOGGER.info("Initializing Rad Gyms mod")

        // Data
        SpeciesManager.register()
        GymManager.register()
        GYM_LOADER.register()

        // Worldgen
        DimensionManager.register()

        // Blocks, items and creative tab
        ItemManager.register()
        BlockManager.register()
        ItemGroupManager.register()

        // Commands
        CommandManager.register()
        RCTCommandManager.register()

        // Events
        RCTEventManager.register()

        // Network
        NetworkStackProcessor.register()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun modIdentifier(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }
}