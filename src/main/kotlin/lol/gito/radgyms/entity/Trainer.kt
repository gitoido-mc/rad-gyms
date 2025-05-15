package lol.gito.radgyms.entity

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.trainer.TrainerNPC
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.gym.GymManager.PLAYER_GYMS
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
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
        if (player is ServerPlayerEntity && world is ServerWorld) {
            val trainerRegistry = RadGyms.RCT.trainerRegistry
            val rctBattleManager = RadGyms.RCT.battleManager
            val playerTrainer = trainerRegistry.getById(player.uuid.toString())
            val gymNpcPair = PLAYER_GYMS[player.uuid]!!.npcList.first { it.first == uuid }

            var npcTrainer: TrainerNPC?
            RadGyms.RCT.trainerRegistry.let { registry ->
                val entity = this
                npcTrainer = when (RadGyms.RCT.trainerRegistry.getById(uuid.toString())) {
                    null -> registry.registerNPC(uuid.toString(), gymNpcPair.second.trainer).apply {
                        this.entity = entity
                    }

                    else -> registry.getById(uuid.toString()) as TrainerNPC
                }
            }

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
                return ActionResult.FAIL
            }

            rctBattleManager.startBattle(
                listOf(playerTrainer),
                listOf(npcTrainer),
                BattleFormat.GEN_9_SINGLES,
                BattleRules()
            )

            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
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
}
