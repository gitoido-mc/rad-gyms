package lol.gito.radgyms.client.model

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib.model.DefaultedBlockGeoModel

class GymEntranceModel : DefaultedBlockGeoModel<GymEntranceEntity>(modId("gym_entrance")) {
    private val modelPath = buildFormattedModelPath(
        modId("cobblemon/gym_entrance")
    )
    private val texturePath = buildFormattedTexturePath(
        modId("cobblemon/gym_entrance_default")
    )
    private val animationsPath = buildFormattedAnimationPath(
        modId("cobblemon/gym_entrance")
    )

    override fun getModelResource(animatable: GymEntranceEntity): Identifier {
        return modelPath
    }

    override fun getTextureResource(animatable: GymEntranceEntity): Identifier {
        return texturePath
    }

    override fun getAnimationResource(animatable: GymEntranceEntity): Identifier {
        return animationsPath
    }
}
