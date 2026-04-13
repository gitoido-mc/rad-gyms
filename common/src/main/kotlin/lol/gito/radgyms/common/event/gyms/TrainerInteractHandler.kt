/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.entity.npc.NPCEntity
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
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.RadGyms.warn
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class TrainerInteractHandler(val event: GymEvents.TrainerInteractEvent) {
    private val checkTrainerDefeated: Boolean = when(event.trainer) {
        is Trainer -> event.trainer.defeated
        else -> false
    }

    init {
        if (event.trainer is NPCEntity) {
            val gym = RadGymsState.getGymForPlayer(event.player)
            val trainerModel = RCT.trainerRegistry.getById(event.player.uuid.toString())
            val gymConfig = gym
                ?.template
                ?.trainers
                ?.firstOrNull { it.id == event.trainer.resourceIdentifier.path }!!


            val npcModel: TrainerNPC = when (val model = RCT.trainerRegistry.getById(event.trainer.uuid.toString())) {
                null -> RCT.trainerRegistry.registerNPC(event.trainer.uuid.toString(), gymConfig.trainer)
                else -> model
            } as TrainerNPC

            npcModel.entity = event.trainer

            RCT.battleManager.startBattle(
                listOf(trainerModel),
                listOf(npcModel),
                gymConfig.format,
                gymConfig.battleRules,
            )
        }

        if (event.trainer is Trainer) {
            debug("Checking required: ${event.trainer.requires}")
            when (event.trainer.requires != null) {
                false -> handleNoRequired()
                true -> handleRequired(
                    (event.trainer.level() as ServerLevel).getEntity(event.trainer.requires!!) as Trainer,
                )
            }
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
            event.player.displayClientMessage(tl(modId("message.info.trainer_required"), required.name))
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
                handleBattleStartError(modId("message.error.battle_start_invalid_entity"), event.player)
                true
            }

            (gym == null) -> {
                warn("Player {} tried to initialize battle without proper gym instance", event.player.uuid)
                handleBattleStartError(modId("message.error.player_gym_state_empty"), event.player)
                true
            }

            (event.trainer.uuid !in gym.npcList) -> {
                warn(
                    "Gym instance for player {} does not contain information about trainer {}",
                    event.player.uuid,
                    event.trainer.uuid,
                )
                handleBattleStartError(modId("message.error.missing_trainer_id_gym_state"), event.player)
                true
            }

            else -> false
        }

        if (shouldReturn) return

        if (event.trainer is Trainer) {
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
    }

    private fun getSides() = with(RCT.trainerRegistry) {
        val playerTrainer = getById(event.player.uuid.toString())
        val trainer = event.trainer as Trainer
        val npcTrainer: TrainerNPC = try {
                registerNPC(
                    trainer.stringUUID,
                    TrainerModel(
                        trainer.name.string,
                        JTO.of { RCTBattleAI(RCTBattleAIConfig.Builder().build()) },
                        trainer.configuration.bag,
                        trainer.configuration.team,
                    ),
                )
        } catch (_: IllegalArgumentException) {
            getById(trainer.stringUUID, TrainerNPC::class.java)
        } as TrainerNPC

        npcTrainer.entity = trainer

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
        if (event.trainer is Trainer) {
            val messageKey = when (event.trainer.leader) {
                true -> "message.info.leader_defeated"
                false -> "message.info.trainer_defeated"
            }

            event.player.displayClientMessage(tl(modId(messageKey)))
        }
    }

    private fun handleBattleStartError(message: ResourceLocation, player: ServerPlayer) {
        player.displayClientMessage(tl(message))
        player.giveOrDropItemStack(EXIT_ROPE.defaultInstance, true)
    }
}
