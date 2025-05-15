package lol.gito.radgyms.item.block

import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.block.BlockRegistry
import net.minecraft.client.render.item.BuiltinModelItemRenderer
import net.minecraft.item.BlockItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.SingletonGeoAnimatable
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.*
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import software.bernie.geckolib.util.RenderUtil
import java.util.function.Consumer

class GymEntranceItem : BlockItem, GeoItem {
    private val cache = GeckoLibUtil.createInstanceCache(this)

    private val idleAnim = RawAnimation.begin().thenLoop("animation.gym_entrance.idle")

    constructor(settings: Settings) : super(BlockRegistry.GYM_ENTRANCE, settings) {
        SingletonGeoAnimatable.registerSyncedAnimatable(this)
    }

    private fun variadicAnimController(state: AnimationState<GymEntranceItem>): PlayState {
        return state.setAndContinue(idleAnim)
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        val defaultAnimController = AnimationController(
            this,
            "gym_entrance",
            ::variadicAnimController
        )
        controllers.add(defaultAnimController)
    }

    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: GeoItemRenderer<GymEntranceItem>? = null

            override fun getGeoItemRenderer(): BuiltinModelItemRenderer? {
                if (renderer == null) {
                    renderer = GeoItemRenderer<GymEntranceItem>(
                        DefaultedBlockGeoModel(
                            modId("gym_entrance"),
                        )
                    )
                }
                return renderer
            }
        })
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return cache
    }

    override fun getTick(itemStack: Any): Double {
        return RenderUtil.getCurrentTick()
    }
}
