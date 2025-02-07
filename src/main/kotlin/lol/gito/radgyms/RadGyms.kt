package lol.gito.radgyms

import com.gitlab.srcmc.rctapi.api.RCTApi
import io.wispforest.owo.network.OwoNetChannel
import lol.gito.radgyms.block.BlockManager
import lol.gito.radgyms.block.entity.BlockEntityRegistry
import lol.gito.radgyms.command.CommandManager
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.event.EventManager
import lol.gito.radgyms.gym.GymLoader
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.item.ItemGroupManager
import lol.gito.radgyms.item.ItemManager
import lol.gito.radgyms.network.NetworkStackHandler
import lol.gito.radgyms.gym.SpeciesManager
import lol.gito.radgyms.world.DimensionManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.VillagerEntityRenderer
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
        LOGGER.info("Initializing the mod")

        // Data
        EntityManager.register()
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

        // Events
        EventManager.register()

        // Network
        NetworkStackHandler.register()

        onInitializeClient()
    }

    @Environment(EnvType.CLIENT)
    fun onInitializeClient() {
        LOGGER.info("Initializing client")
        EntityRendererRegistry.register(EntityManager.GYM_TRAINER) {context -> VillagerEntityRenderer(context) }
    }

    fun modIdentifier(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }
}