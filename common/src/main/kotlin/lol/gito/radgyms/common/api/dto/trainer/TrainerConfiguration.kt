/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.dto.trainer

import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.extension.nbt.*
import net.minecraft.nbt.CompoundTag

data class TrainerConfiguration(
    val battleRules: BattleRules,
    val bag: MutableList<BagItemModel> = mutableListOf(),
    val team: MutableList<PokemonModel> = mutableListOf(),
) {
    companion object {
        fun fromCompoundTag(tag: CompoundTag): TrainerConfiguration =
            TrainerConfiguration(
                tag.getRadGymsBattleRules("BattleRules"),
                tag.getRadGymsTrainerBag("Bag"),
                tag.getRadGymsTrainerTeam("Team"),
            )
    }

    fun toCompoundTag(): CompoundTag {
        val tag = CompoundTag()

        tag.putRadGymsBattleRules("BattleRules", battleRules)
        tag.putRadGymsTrainerBag("Bag", bag)
        tag.putRadGymsTrainerTeam("Team", team)

        return tag
    }
}
