package lol.gito.radgyms.common.extension.nbt

import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import net.minecraft.nbt.CompoundTag

fun CompoundTag.getRadGymsBattleRules(key: String): BattleRules? {
    val nbt = this.getCompound(key) ?: return null

    return BattleRules.Builder()
        .withMaxItemUses(nbt.getInt("MaxItemUses"))
        .withHealPlayers(nbt.getBoolean("HealPlayers"))
        .withAdjustPlayerLevels(nbt.getBoolean("AdjustPlayerLevels"))
        .withAdjustNPCLevels(nbt.getBoolean("AdjustNpcLevels"))
        .build()
}

fun CompoundTag.putRadGymsBattleRules(key: String, value: BattleRules) {
    val nbt = CompoundTag()

    nbt.putInt("MaxItemUses", value.maxItemUses)
    nbt.putBoolean("HealPlayers", value.healPlayers)
    nbt.putBoolean("AdjustPlayerLevels", value.adjustPlayerLevels)
    nbt.putBoolean("AdjustNpcLevels", value.adjustNPCLevels)

    this.put(key, nbt)
}
