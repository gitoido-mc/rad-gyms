/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event.gyms

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.api.enumeration.GymBattleFormat
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.common.entity.Trainer
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class TrainerInteractHandler(event: GymEvents.TrainerInteractEvent) {
    init {
        var requiredTrainer: Trainer? = null
        if (event.trainer.requires != null) {
            debug("Trainer has linked trainer in props, fetching...")
            requiredTrainer = (event.trainer.world as ServerWorld).getEntity(event.trainer.requires) as Trainer
        }

        if (requiredTrainer != null && !requiredTrainer.defeated) {
            debug("Linked trainer is not defeated yet, sending chat message")
            event.player.sendMessage(
                Text.translatable(
                    "rad-gyms.message.info.trainer_required",
                    requiredTrainer.name
                ),
                true
            )
            event.cancel()
        }

        if (event.trainer.defeated) {
            val messageKey = when (event.trainer.leader) {
                true -> "rad-gyms.message.info.leader_defeated"
                false -> "rad-gyms.message.info.trainer_defeated"
            }

            event.player.sendMessage(Text.translatable(messageKey), true)
            event.cancel()
        } else {
            val trainerRegistry = RCT.trainerRegistry
            val rctBattleManager = RCT.battleManager
            val playerTrainer = trainerRegistry.getById(event.player.uuid.toString())
            val npcTrainer: TrainerNPC = trainerRegistry.getById(event.trainer.uuid.toString(), TrainerNPC::class.java)

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
    }
}