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
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.VillagerEntityRenderer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object RadGymsClient : ClientModInitializer {
    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        RadGyms.LOGGER.info("Initializing client")
        EntityRendererRegistry.register(EntityManager.GYM_TRAINER) {context -> VillagerEntityRenderer(context) }
    }

}