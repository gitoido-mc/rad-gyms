/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.server.event.gyms

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.api.events.ModEvents
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.entity.Trainer
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class TrainerInteractHandler(event: ModEvents.TrainerInteractEvent) {
    init {
        var requiredTrainer: Trainer? = null
        if (event.trainer.requires != null) {
            requiredTrainer = (event.trainer.world as ServerWorld).getEntity(event.trainer.requires) as Trainer
        }

        if (requiredTrainer != null && !requiredTrainer.defeated) {
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
        }

        if (!event.isCanceled) {
            val trainerRegistry = RadGyms.RCT.trainerRegistry
            val rctBattleManager = RadGyms.RCT.battleManager
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
                BattleFormat.GEN_9_SINGLES,
                BattleRules()
            )
        }
    }
}