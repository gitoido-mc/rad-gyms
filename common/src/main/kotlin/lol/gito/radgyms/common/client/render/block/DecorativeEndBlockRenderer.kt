/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import lol.gito.radgyms.common.block.entity.DecorativeEndPortalEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer

class DecorativeEndBlockRenderer(context: BlockEntityRendererProvider.Context) : TheEndPortalRenderer<DecorativeEndPortalEntity>(context) {
    override fun render(
        theEndGatewayBlockEntity: DecorativeEndPortalEntity,
        f: Float,
        poseStack: PoseStack,
        multiBufferSource: MultiBufferSource,
        i: Int,
        j: Int,
    ) {
        super.render(theEndGatewayBlockEntity, f, poseStack, multiBufferSource, i, j)
    }

    override fun getOffsetDown() = 1f

    override fun getOffsetUp() = 1f
}
