package lol.gito.radgyms.item

import lol.gito.radgyms.item.group.ItemGroupManager
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Rarity


class EpicPokeShard: PokeShardBase(Rarity.EPIC)

class RarePokeShard: PokeShardBase(Rarity.RARE)

class UncommonPokeShard: PokeShardBase(Rarity.UNCOMMON)

class CommonPokeShard: PokeShardBase(Rarity.COMMON)

open class PokeShardBase(private val rarity: Rarity) : Item(
    Settings().group(ItemGroupManager.GYMS_GROUP).rarity(rarity)
) {
    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.set(DataComponentTypes.RARITY, this.rarity)

        return stack
    }
}
