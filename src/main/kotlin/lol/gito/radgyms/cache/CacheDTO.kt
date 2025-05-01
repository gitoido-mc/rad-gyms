package lol.gito.radgyms.cache

import kotlinx.serialization.Serializable
import net.minecraft.util.Rarity
import net.minecraft.util.collection.WeightedList

@Serializable
class CacheDTO(
    val common: HashMap<String, Int>,
    val uncommon: HashMap<String, Int>,
    val rare: HashMap<String, Int>,
    val epic: HashMap<String, Int>,
) {
    fun forRarity(rarity: Rarity): WeightedList<String> {
        val pokeList = when (rarity) {
            Rarity.COMMON -> this.common
            Rarity.UNCOMMON -> this.uncommon + this.common
            Rarity.RARE -> this.rare + this.uncommon
            Rarity.EPIC -> this.epic + this.rare
        }
        val list = WeightedList<String>()
        for (pokeItem in pokeList) {
            list.add(pokeItem.key, pokeItem.value)
        }
        return list
    }
}


