package lol.gito.radgyms.common.api.dto.trainer

import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.extension.nbt.getRadGymsBattleRules
import lol.gito.radgyms.common.extension.nbt.getRadGymsTrainerBag
import lol.gito.radgyms.common.extension.nbt.getRadGymsTrainerTeam
import lol.gito.radgyms.common.extension.nbt.putRadGymsBattleRules
import lol.gito.radgyms.common.extension.nbt.putRadGymsTrainerBag
import lol.gito.radgyms.common.extension.nbt.putRadGymsTrainerTeam
import net.minecraft.nbt.CompoundTag

data class TrainerConfiguration(
    val battleRules: BattleRules,
    val bag: MutableList<BagItemModel> = mutableListOf(),
    val team: MutableList<PokemonModel> = mutableListOf()
) {
    companion object {
        fun fromCompoundTag(tag: CompoundTag): TrainerConfiguration {
            return TrainerConfiguration(
                tag.getRadGymsBattleRules("BattleRules"),
                tag.getRadGymsTrainerBag("Bag"),
                tag.getRadGymsTrainerTeam("Team")
            )
        }
    }

    fun toCompoundTag(): CompoundTag {
        val tag = CompoundTag()

        tag.putRadGymsBattleRules("BattleRules", battleRules)
        tag.putRadGymsTrainerBag("Bag", bag)
        tag.putRadGymsTrainerTeam("Team", team)

        return tag
    }
}
