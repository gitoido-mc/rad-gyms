/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.team

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import net.minecraft.server.level.ServerPlayer

interface TeamGeneratorInterface {
    fun generateTeam(
        trainer: TrainerModel.Json.Trainer,
        level: Int,
        player: ServerPlayer,
        possibleFormats: MutableList<GymBattleFormat>? = mutableListOf(GymBattleFormat.SINGLES),
        types: List<ElementalType>? = listOf(ElementalTypes.getRandomType())
    ): MutableList<PokemonModel>

    fun generatePokemon(
        level: Int,
        thresholdAmount: Int,
        type: ElementalType = ElementalTypes.getRandomType(),
    ): Pokemon
}