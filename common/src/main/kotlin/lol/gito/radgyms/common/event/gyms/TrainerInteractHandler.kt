/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.event.gyms

import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.util.asUUID
import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.gitlab.srcmc.rctapi.api.battle.BattleManager
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.battle.BattleState
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.ASPECT_REQUIRED
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.RadGyms.warn
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.extension.cobblemon.npc.isDefeated
import lol.gito.radgyms.common.extension.cobblemon.npc.isLeader
import lol.gito.radgyms.common.extension.displayClientMessage
import lol.gito.radgyms.common.helper.tl
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.world.state.RadGymsState
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import java.util.*
import lol.gito.radgyms.common.api.dto.trainer.TrainerModel as RGModel

class TrainerInteractHandler(val event: GymEvents.TrainerInteractEvent) {
//    private fun NPCEntity.rctTrainer(): TrainerNPC {
//        return when (val model = RCT.trainerRegistry.getById(this.uuid.toString())) {
//            null -> RCT.trainerRegistry.registerNPC(this.uuid.toString(), event.trainer.rgModel()!!.trainer)
//                .also { model ->
//                    model.entity = this
//                }
//
//            else -> model
//        } as TrainerNPC
//    }

    private fun NPCEntity.rgModel(): RGModel? {
        val gym = RadGymsState.getGymForPlayer(event.player)
        return gym
            ?.template
            ?.trainers
            ?.firstOrNull { it.id == this.resourceIdentifier.path }
    }

    init {

        val required = required()
        debug("Checking required: ${required?.uuid}")

        when (required != null) {
            false -> handleNoRequired()
            true -> handleRequired(required)
        }
    }

    private fun handleNoRequired() {
        if (!event.trainer.isDefeated) {
            startBattle()
            return
        }
        notifyPlayer()
        event.cancel()
    }

    private fun handleRequired(required: NPCEntity) {
        debug("required is: ${required.uuid}, defeated?: ${required.isDefeated}")
        if (!required.isDefeated) {
            debug("Linked trainer is not defeated yet, sending chat message")
            event.player.displayClientMessage(tl(modId("message.info.trainer_required"), required.name))
            event.cancel()
            return
        }

        if (event.trainer.isDefeated) {
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



        with(RCT.battleManager) {
            // Check for being in battle just in case
            // Force all battles for player to end
            states.forEach { state -> finalizeState(state, this) }
            val trainer = event.trainer.rgModel()!!
            debug(
                "Starting {} battle between player {} and trainer {}",
                trainer.format,
                event.player.displayName?.string as Any,
                event.trainer.displayName?.string as Any,
            )

            val sides = getSides()
            startBattle(
                listOf(sides.first),
                listOf(sides.second),
                trainer.format,
                BattleRules(),
            )
        }
    }

    private fun getSides() = with(RCT.trainerRegistry) {
        val playerTrainer = getById(event.player.uuid.toString())
        val model = event.trainer.rgModel()!!
        val npcTrainer: TrainerNPC = try {
            registerNPC(
                event.trainer.stringUUID,
                model.trainer
            )
        } catch (_: IllegalArgumentException) {
            getById(event.trainer.stringUUID, TrainerNPC::class.java)
        } as TrainerNPC

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
        val messageKey = when (event.trainer.isLeader) {
            true -> "message.info.leader_defeated"
            false -> "message.info.trainer_defeated"
        }

        event.player.displayClientMessage(tl(modId(messageKey)))
    }

    private fun handleBattleStartError(message: ResourceLocation, player: ServerPlayer) {
        player.displayClientMessage(tl(message))
        player.giveOrDropItemStack(EXIT_ROPE.defaultInstance, true)
    }

    private fun required(): NPCEntity? {
        return when (val aspect = event.trainer.aspects.firstOrNull { it.startsWith(ASPECT_REQUIRED) }) {
            null -> null
            else -> {
                val uuid: UUID = aspect.replace(ASPECT_REQUIRED, "").asUUID!!

                (event.trainer.level() as ServerLevel).getEntity(uuid) as NPCEntity?
            }
        }
    }
}
