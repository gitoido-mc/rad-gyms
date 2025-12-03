/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.client

import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.client.RadGymsClient
import lol.gito.radgyms.common.client.RadGymsClientImplementation
import lol.gito.radgyms.fabric.RadGymsFabric
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@Environment(EnvType.CLIENT)
object RadGymsFabricClient : RadGymsClientImplementation {
    fun modModelId(id: ResourceLocation, variant: String): ModelResourceLocation = ModelResourceLocation(id, variant)

    fun initialize() {
        RadGyms.debug("Initializing client")
        RadGymsClient.initialize(this)
        RadGymsFabric.networkManager.registerClientHandlers()
    }

    override fun registerBlockRenderType(
        layer: RenderType,
        vararg blocks: Block
    ) = blocks.forEach {
        BlockRenderLayerMap.INSTANCE.putBlock(it, layer)
    }

    override fun <T : BlockEntity> registerBlockEntityRenderer(
        type: BlockEntityType<out T>,
        factory: BlockEntityRendererProvider<T>
    ) = BlockEntityRenderers.register(type, factory)

    override fun <T : Entity> registerEntityRenderer(
        type: EntityType<out T>,
        factory: EntityRendererProvider<T>
    ) = EntityRendererRegistry.register(type, factory)
}