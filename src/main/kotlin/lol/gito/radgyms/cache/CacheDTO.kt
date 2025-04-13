package lol.gito.radgyms.cache

import kotlinx.serialization.Serializable
import net.minecraft.util.Rarity

@Serializable
class CacheDTO(
    val common: HashMap<String, Int>,
    val uncommon: HashMap<String, Int>,
    val rare: HashMap<String, Int>,
    val epic: HashMap<String, Int>,
) {
    fun forRarity(rarity: Rarity): Map<String, Int> {
        return when (rarity) {
            Rarity.COMMON -> this.common
            Rarity.UNCOMMON -> this.uncommon + this.common
            Rarity.RARE -> this.rare + this.uncommon + this.common
            Rarity.EPIC -> this.epic
        }
    }
}


