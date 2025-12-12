/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.TrainerFactory
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.util.displayClientMessage
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerLevel

class TrainerInteractHandler(event: GymEvents.TrainerInteractEvent) {
    private val checkTrainerDefeated: Boolean = event.trainer.defeated

    init {
        debug("Checking required: ${event.trainer.requires}")
        when (event.trainer.requires != null) {
            false -> handleNoRequired(event)
            true -> handleRequired(
                event,
                (event.trainer.level() as ServerLevel).getEntity(event.trainer.requires!!) as Trainer
            )
        }
    }

    private fun handleNoRequired(event: GymEvents.TrainerInteractEvent) {
        if (!checkTrainerDefeated) {
            startBattle(event)
            return
        }
        notifyPlayer(event)
        event.cancel()
    }

    private fun handleRequired(event: GymEvents.TrainerInteractEvent, required: Trainer) {
        debug("required is: ${required.uuid}, defeated?: ${required.defeated}")
        if (!required.defeated) {
            debug("Linked trainer is not defeated yet, sending chat message")
            event.player.displayClientMessage(
                translatable("rad-gyms.message.info.trainer_required", required.name)
            )
            event.cancel()
            return
        }

        if (checkTrainerDefeated) {
            notifyPlayer(event)
            event.cancel()
            return
        }

        startBattle(event)
    }

    private fun startBattle(event: GymEvents.TrainerInteractEvent) {
        val trainerRegistry = RCT.trainerRegistry
        val rctBattleManager = RCT.battleManager
        val playerTrainer = trainerRegistry.getById(event.player.uuid.toString())
        val npcTrainer: TrainerNPC =
            trainerRegistry.getById(event.trainer.uuid.toString(), TrainerNPC::class.java)


        npcTrainer.entity = event.trainer

        // Check for being in battle just in case
        // Force all battles for player to end
        rctBattleManager.apply {
            this.states.forEach { state ->
                state.battle.let { battle ->
                    val actor = battle.actors.firstOrNull { actor -> actor.isForPlayer(event.player) }
                    if (actor != null && battle.ended) {
                        debug("Uh-oh, found stuck battle for player actor, ending it")
                        battle.end()
                        rctBattleManager.end(battle.battleId, true)
                    }
                }
            }
        }

        debug("Starting {} battle between player {} and trainer {}",
            event.trainer.format,
            event.trainer.displayName?.string as Any,
            event.player.displayName?.string as Any
        )

        rctBattleManager.startBattle(
            listOf(playerTrainer),
            listOf(npcTrainer),
            when (GymBattleFormat.valueOf(event.trainer.format)) {
                GymBattleFormat.SINGLES -> BattleFormat.GEN_9_SINGLES
                GymBattleFormat.DOUBLES -> BattleFormat.GEN_9_DOUBLES
                GymBattleFormat.TRIPLES -> BattleFormat.GEN_9_TRIPLES
            },
            BattleRules()
        )
    }

    private fun notifyPlayer(event: GymEvents.TrainerInteractEvent) {
        val messageKey = when (event.trainer.leader) {
            true -> "rad-gyms.message.info.leader_defeated"
            false -> "rad-gyms.message.info.trainer_defeated"
        }

        event.player.displayClientMessage(translatable(messageKey))
    }
}