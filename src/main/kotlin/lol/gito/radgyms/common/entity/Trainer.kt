/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.entity

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.debug
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class Trainer(entityType: EntityType<out Trainer>, world: World) : VillagerEntity(entityType, world) {
    private val trainerIdKey = "trainerId"
    private val requiresKey = "requires"
    private val defeatedKey = "isDefeated"
    private val leaderKey = "isLeader"

    var trainerId: UUID? = null
    var requires: UUID? = null
    var defeated: Boolean = false
    var leader: Boolean = false

    init {
        this.world = world
        this.setPersistent()
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = createVillagerAttributes()
    }

    override fun getMaxLookYawChange(): Int = 360
    override fun isSilent(): Boolean = true
    override fun isPushable(): Boolean = false
    override fun damage(source: DamageSource, amount: Float): Boolean = false

    override fun tickMovement() {
        super.tickMovement()
        velocity = Vec3d.ZERO
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        if (!this.world.isClient) {
            val player = player as ServerPlayerEntity
            if (requires != null) {
                val trainerToFight = (world as ServerWorld).getEntity(requires) as Trainer

                if (!trainerToFight.defeated) {
                    player.sendMessage(
                        Text.translatable(
                            "rad-gyms.message.info.trainer_required",
                            trainerToFight.name
                        ),
                        true
                    )
                    return ActionResult.FAIL
                }
            }

            if (defeated) {
                val messageKey = when (leader) {
                    true -> "rad-gyms.message.info.leader_defeated"
                    false -> "rad-gyms.message.info.trainer_defeated"
                }

                player.sendMessage(Text.translatable(messageKey), true)
                sayNo()
                return ActionResult.FAIL
            }

            val trainerRegistry = RadGyms.RCT.trainerRegistry
            val rctBattleManager = RadGyms.RCT.battleManager
            val playerTrainer = trainerRegistry.getById(player.uuid.toString())
            val npcTrainer: TrainerNPC = trainerRegistry.getById(uuid.toString(), TrainerNPC::class.java)

            // Check for being in battle just in case
            // Force all battles for player to end
            rctBattleManager.apply {
                this.states.forEach { state ->
                    state.battle.apply {
                        val actor = this.actors.firstOrNull { actor -> actor.isForPlayer(player) }
                        if (actor != null && !this.ended) {
                            debug("Uh-oh, found stuck battle for player actor, ending it")
                            this.end()
                            rctBattleManager.end(this.battleId, true)
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
        } else {
            if (defeated) {
                sayNo()
                return ActionResult.FAIL
            }
        }

        return ActionResult.success(this.world.isClient)
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        nbt.putBoolean(leaderKey, leader)
        nbt.putBoolean(defeatedKey, defeated)

        if (trainerId != null) {
            nbt.putUuid(trainerIdKey, trainerId)
        }
        if (requires != null) {
            nbt.putUuid(requiresKey, requires)
        }

        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (nbt.contains(trainerIdKey)) {
            trainerId = nbt.getUuid(trainerIdKey)
        }
        if (nbt.contains(requiresKey)) {
            requires = nbt.getUuid(requiresKey)
        }
        if (nbt.contains(defeatedKey)) {
            defeated = nbt.getBoolean(defeatedKey)
        }
        if (nbt.contains(leaderKey)) {
            leader = nbt.getBoolean(leaderKey)
        }
    }

    private fun sayNo() {
        this.headRollingTimeLeft = 40
        if (!this.world.isClient) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO)
        }
    }
}
