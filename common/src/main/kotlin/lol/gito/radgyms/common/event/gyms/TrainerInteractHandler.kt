/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import com.gitlab.srcmc.rctapi.api.battle.BattleManager
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.battle.BattleState
import com.gitlab.srcmc.rctapi.api.models.TrainerModel
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.warn
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class TrainerInteractHandler(val event: GymEvents.TrainerInteractEvent) {
    private val checkTrainerDefeated: Boolean = event.trainer.defeated

    init {
        debug("Checking required: ${event.trainer.requires}")
        when (event.trainer.requires != null) {
            false -> handleNoRequired()
            true -> handleRequired(
                (event.trainer.level() as ServerLevel).getEntity(event.trainer.requires!!) as Trainer,
            )
        }
    }

    private fun handleNoRequired() {
        if (!checkTrainerDefeated) {
            startBattle()
            return
        }
        notifyPlayer()
        event.cancel()
    }

    private fun handleRequired(required: Trainer) {
        debug("required is: ${required.uuid}, defeated?: ${required.defeated}")
        if (!required.defeated) {
            debug("Linked trainer is not defeated yet, sending chat message")
            event.player.displayClientMessage(tl("message.info.trainer_required", required.name))
            event.cancel()
            return
        }

        if (checkTrainerDefeated) {
            notifyPlayer()
            event.cancel()
            return
        }

        startBattle()
    }

    private fun startBattle() {
        val gym = RadGymsState.getGymForPlayer(event.player)

        val shouldReturn = when {
            (event.trainer.uuid == null) -> {
                warn(
                    "Player {} tried to initialize battle with trainer entity {} without trainerId",
                    event.player.uuid,
                    event.trainer.uuid,
                )
                handleBattleStartError("message.error.battle_start_invalid_entity", event.player)
                true
            }

            (gym == null) -> {
                warn("Player {} tried to initialize battle without proper gym instance", event.player.uuid)
                handleBattleStartError("message.error.player_gym_state_empty", event.player)
                true
            }

            (event.trainer.uuid !in gym.npcList) -> {
                warn(
                    "Gym instance for player {} does not contain information about trainer {}",
                    event.player.uuid,
                    event.trainer.uuid,
                )
                handleBattleStartError("message.error.missing_trainer_id_gym_state", event.player)
                true
            }

            else -> false
        }

        if (shouldReturn) return

        with(RCT.battleManager) {
            // Check for being in battle just in case
            // Force all battles for player to end
            states.forEach { state -> finalizeState(state, this) }

            debug(
                "Starting {} battle between player {} and trainer {}",
                event.trainer.format,
                event.player.displayName?.string as Any,
                event.trainer.displayName?.string as Any,
            )

            val sides = getSides()
            startBattle(
                listOf(sides.first),
                listOf(sides.second),
                GymBattleFormat.valueOf(event.trainer.format),
                BattleRules(),
            )
        }
    }

    private fun getSides() = with(RCT.trainerRegistry) {
        val playerTrainer = getById(event.player.uuid.toString())
        val npcTrainer: TrainerNPC = try {
            registerNPC(
                event.trainer.stringUUID,
                TrainerModel(
                    event.trainer.name.string,
                    JTO.of { RCTBattleAI(RCTBattleAIConfig.Builder().build()) },
                    event.trainer.configuration.bag,
                    event.trainer.configuration.team,
                ),
            )
        } catch (_: IllegalArgumentException) {
            getById(event.trainer.stringUUID, TrainerNPC::class.java)
        }

        npcTrainer.entity = event.trainer

        return@with playerTrainer to npcTrainer
    }

    private fun finalizeState(state: BattleState, rctBattleManager: BattleManager) = with(state.battle) {
        val actor = actors.firstOrNull { actor -> actor.isForPlayer(event.player) }
        if (actor != null && this.ended) {
            debug("Uh-oh, found stuck battle for player actor, ending it")
            end()
            rctBattleManager.end(battleId, true)
        }
    }

    private fun notifyPlayer() {
        val messageKey = when (event.trainer.leader) {
            true -> "message.info.leader_defeated"
            false -> "message.info.trainer_defeated"
        }

        event.player.displayClientMessage(tl(messageKey))
    }

    private fun handleBattleStartError(message: String, player: ServerPlayer) {
        player.displayClientMessage(tl(message))
        player.giveOrDropItemStack(EXIT_ROPE.defaultInstance, true)
    }
}
