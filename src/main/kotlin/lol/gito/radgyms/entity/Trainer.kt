package lol.gito.radgyms.entity

import com.gitlab.srcmc.rctapi.api.battle.BattleFormat
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import lol.gito.radgyms.RadGyms
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class Trainer : VillagerEntity
{
    var trainerId: UUID? = null
    var required: UUID? = null

    override fun getMaxLookYawChange(): Int = 360
    override fun isSilent(): Boolean = true
    override fun isPushable(): Boolean = false
    override fun damage(source: DamageSource, amount: Float): Boolean = false

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createVillagerAttributes()
        }
    }

    constructor(entityType: EntityType<out VillagerEntity>, world: World) : super(entityType, world) {
        this.world = world
        this.setPersistent();
    }

    override fun tickMovement() {
        super.tickMovement()
        velocity = Vec3d.ZERO
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        if (player is ServerPlayerEntity && world is ServerWorld) {
            val playerTrainer = RadGyms.RCT.trainerRegistry.getById(player.uuid.toString())
            val npcTrainer = RadGyms.RCT.trainerRegistry.getById(trainerId.toString())

            RadGyms.RCT.battleManager.start(
                listOf(playerTrainer),
                listOf(npcTrainer),
                BattleFormat.GEN_9_SINGLES,
                BattleRules()
            );

            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand)
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        if (trainerId != null) {
            nbt.putUuid(RadGyms.modIdentifier("trainer_id").toString(), trainerId)
        }
        if (required != null) {
            nbt.putUuid(RadGyms.modIdentifier("required_trainer_id").toString(), trainerId)
        }

        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        trainerId = nbt.getUuid(RadGyms.modIdentifier("trainer_id").toString())
        required = nbt.getUuid(RadGyms.modIdentifier("required_trainer_id").toString())
    }
}