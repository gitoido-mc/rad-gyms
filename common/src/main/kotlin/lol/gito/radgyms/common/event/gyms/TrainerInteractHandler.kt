/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.gitlab.srcmc.rctapi.api.battle.BattleManager
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.battle.BattleState
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.warn
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.util.displayClientMessage
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

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
        val gym = RadGymsState.getGymForPlayer(event.player) ?: run {
            warn("Player {} tried to initialize battle without proper gym instance", event.player.uuid)
            handleBattleStartError("rad-gyms.message.error.player_gym_state_empty", event.player)
            return
        }

        if (event.trainer.trainerId == null) {
            warn(
                "Player {} tried to initialize battle with trainer entity {} without trainerId",
                event.player.uuid,
                event.trainer.uuid
            )
            handleBattleStartError("rad-gyms.message.error.battle_start_invalid_entity", event.player)
            return
        }

        if (event.trainer.trainerId !in gym.npcList.keys) {
            warn(
                "Gym instance for player {} does not contain information about trainer {}",
                event.player.uuid,
                event.trainer.uuid
            )
            handleBattleStartError("rad-gyms.message.error.missing_trainer_id_gym_state", event.player)
            return
        }

        val trainerRegistry = RCT.trainerRegistry
        val rctBattleManager = RCT.battleManager
        val playerTrainer = trainerRegistry.getById(event.player.uuid.toString())
        val npcTrainer: TrainerNPC = trainerRegistry.registerNPC(
            event.trainer.trainerId.toString(),
            gym.npcList[event.trainer.trainerId]!!.trainer,
        )

        npcTrainer.entity = event.trainer

        // Check for being in battle just in case
        // Force all battles for player to end
        rctBattleManager.apply {
            this.states.forEach { state ->
                finalizeState(state, event, rctBattleManager)
            }
        }

        debug(
            "Starting {} battle between player {} and trainer {}",
            event.trainer.format,
            event.player.displayName?.string as Any,
            event.trainer.displayName?.string as Any
        )

        rctBattleManager.startBattle(
            listOf(playerTrainer),
            listOf(npcTrainer),
            GymBattleFormat.valueOf(event.trainer.format),
            BattleRules()
        )
    }

    private fun finalizeState(
        state: BattleState,
        event: GymEvents.TrainerInteractEvent,
        rctBattleManager: BattleManager
    ) {
        state.battle.let { battle ->
            val actor = battle.actors.firstOrNull { actor -> actor.isForPlayer(event.player) }
            if (actor != null && battle.ended) {
                debug("Uh-oh, found stuck battle for player actor, ending it")
                battle.end()
                rctBattleManager.end(battle.battleId, true)
            }
        }
    }

    private fun notifyPlayer(event: GymEvents.TrainerInteractEvent) {
        val messageKey = when (event.trainer.leader) {
            true -> "rad-gyms.message.info.leader_defeated"
            false -> "rad-gyms.message.info.trainer_defeated"
        }

        event.player.displayClientMessage(translatable(messageKey))
    }

    private fun handleBattleStartError(message: String, player: ServerPlayer) {
        player.displayClientMessage(translatable(message))
        player.giveOrDropItemStack(EXIT_ROPE.defaultInstance, true)
    }
}
