/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.neoforge.client

import lol.gito.radgyms.common.api.client.RadGymsClientImplementation
import lol.gito.radgyms.common.client.RadGymsClient
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object RadGymsNeoForgeClient : RadGymsClientImplementation {
    fun init() {
        with(MOD_BUS) {
            addListener(::onClientSetup)
        }
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            RadGymsClient.initialize(this)
        }
    }

    @Suppress("DEPRECATION")
    override fun registerBlockRenderType(
        layer: RenderType,
        vararg blocks: Block
    ) {
        blocks.forEach { block ->
            ItemBlockRenderTypes.setRenderLayer(block, layer)
        }
    }

    override fun <T : BlockEntity> registerBlockEntityRenderer(
        type: BlockEntityType<out T>,
        factory: BlockEntityRendererProvider<T>
    ) {
        BlockEntityRenderers.register(type, factory)
    }

    override fun <T : Entity> registerEntityRenderer(
        type: EntityType<out T>,
        factory: EntityRendererProvider<T>
    ) {
        EntityRenderers.register(type, factory)
    }

    internal fun registerResourceReloader(reloader: PreparableReloadListener) {
        (Minecraft.getInstance().resourceManager as ReloadableResourceManager).registerReloadListener(reloader)
    }
}
