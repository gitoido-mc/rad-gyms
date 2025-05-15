package lol.gito.radgyms.client.renderer

import lol.gito.radgyms.block.entity.GymEntranceEntity
import lol.gito.radgyms.client.model.GymEntranceModel
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import software.bernie.geckolib.renderer.GeoBlockRenderer

class GymEntranceEntityRenderer(context: BlockEntityRendererFactory.Context) : GeoBlockRenderer<GymEntranceEntity>(
    GymEntranceModel()
)
