package lol.gito.radgyms.client.renderer

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

@Suppress("UNUSED_PARAMETER")
class GymEntranceEntityRenderer(context: BlockEntityRendererFactory.Context) : GeoBlockRenderer<GymEntranceEntity>(
    DefaultedBlockGeoModel(
        modId("gym_entrance"),
    )
)
