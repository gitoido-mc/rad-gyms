package lol.gito.radgyms

import com.gitlab.srcmc.rctapi.api.RCTApi
import io.wispforest.owo.network.OwoNetChannel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import lol.gito.radgyms.block.BlockManager
import lol.gito.radgyms.command.CommandRegistry
import lol.gito.radgyms.entity.EntityManager
import lol.gito.radgyms.event.EventManager
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.gym.SpeciesManager
import lol.gito.radgyms.item.ItemManager
import lol.gito.radgyms.item.dataComponent.DataComponentManager
import lol.gito.radgyms.item.group.ItemGroupManager
import lol.gito.radgyms.network.NetworkStackHandler
import lol.gito.radgyms.world.DimensionManager
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object RadGyms {
    const val MOD_ID: String = "rad-gyms"
    private const val CONFIG_PATH: String = "config/${MOD_ID}_server.json"
    lateinit var CONFIG: RadGymsConfig
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val CHANNEL: OwoNetChannel = OwoNetChannel.create(modId("main"))
    val RCT: RCTApi = RCTApi.initInstance(MOD_ID)
    private val GYM_LOADER: RadGymsDataLoader = RadGymsDataLoader()

    fun init() {
        LOGGER.info("Initializing the mod")
        loadConfig()
        // Data
        EntityManager.register()
        GymManager.register()
        GYM_LOADER.register()

        // Events
        EventManager.register()

        // Species
        SpeciesManager.register()

        // Worldgen
        DimensionManager.register()

        // Blocks, items and creative tab
        DataComponentManager.register()
        ItemManager.register()
        BlockManager.register()
        ItemGroupManager.register()

        // Commands
        CommandRegistry.register()

        // Network
        NetworkStackHandler.register()
    }

    fun modId(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }

    fun debug(message: String) {
        if (CONFIG.debug == true) LOGGER.info(message)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        CONFIG = if (configFile.exists()) {
            LOGGER.info("Loading config")
            configFile.inputStream().let {
                Json.decodeFromStream<RadGymsConfig>(it)
            }
        } else {
            LOGGER.info("Creating config")
            RadGymsConfig(
                debug = false,
                maxEntranceUses = 3,
                lapisBoostAmount = 1,
                shardRewards = true,
                ignoredSpecies = emptyList(),
                ignoredForms = mutableListOf("gmax"),
            )
        }
        saveConfig()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveConfig() {
        val configFile = File(CONFIG_PATH)
        val prettify = Json {
            prettyPrint = true
        }
        configFile.outputStream().let {
            prettify.encodeToStream(CONFIG, it)
            debug("Saving config")
        }
    }
}
