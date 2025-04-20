package lol.gito.radgyms.gui

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Component
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import lol.gito.radgyms.gui.GymGUIIdentifiers.ID_TYPES
import lol.gito.radgyms.gui.GymGUIIdentifiers.UI_CACHE_ATTUNE
import lol.gito.radgyms.gui.widget.CacheInfoHolder
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Rarity

class CacheAttuneScreen(
    val player: PlayerEntity,
    val rarity: Rarity,
) :
    BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(UI_CACHE_ATTUNE)) {
    lateinit var root: FlowLayout

    override fun build(root: FlowLayout) {
        this.root = root

        root.childById(FlowLayout::class.java, ID_TYPES).children(buildElementalTypesCollection())
        root.childById(FlowLayout::class.java,"cache")
    }

    private fun buildElementalTypesCollection(): MutableCollection<out Component> {
        val collection: MutableCollection<Component> = mutableListOf()

        for (type in ElementalTypes.all()) {
            collection.add(buildElementalTypeComposable(type))
        }

        return collection
    }

    private fun buildElementalTypeComposable(type: ElementalType): Component {
        val panel = CacheInfoHolder(type, rarity, Sizing.expand(), Sizing.fixed(50))
        panel.padding(Insets.vertical(2))
        panel.id("cache")
        panel.build()

        return panel
    }
}
