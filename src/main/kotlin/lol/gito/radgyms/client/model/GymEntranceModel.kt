package lol.gito.radgyms.client.model

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.entity.GymEntranceEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib.model.GeoModel

object GymEntranceModel : GeoModel<GymEntranceEntity>() {
    private val model: Identifier = modId("cobblemon/geo/gym_entrance.geo.json")
    private val texture: Identifier = modId("cobblemon/textures/gym_entrance.png")
    private val animations: Identifier = modId("cobblemon/animations/gym_entrance.animation.json")

    @Deprecated("Deprecated in Java")
    override fun getModelResource(animatable: GymEntranceEntity): Identifier {
        return model
    }

    @Deprecated("Deprecated in Java")
    override fun getTextureResource(animatable: GymEntranceEntity): Identifier {
        return texture
    }

    override fun getAnimationResource(animatable: GymEntranceEntity): Identifier {
        return animations
    }
}
