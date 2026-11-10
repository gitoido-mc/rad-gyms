/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.gym.team

import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import lol.gito.radgyms.common.api.dto.trainer.Trainer
import lol.gito.radgyms.common.api.event.GymEvents
import net.minecraft.server.level.ServerPlayer

object FixedTeamGenerator : GenericTeamGenerator() {
    fun generateTeam(player: ServerPlayer?, trainer: Trainer, level: Int): MutableList<PokemonModel> {
        val rawTeam = rawTeam(trainer, level)

        val event = GymEvents.GenerateTeamEvent(
            player,
            listOf(),
            level,
            trainer.id,
            trainer.leader,
            rawTeam,
            trainer.possibleFormats.toMutableList(),
        )

        return generateTeamPost(event, false)
    }
}
