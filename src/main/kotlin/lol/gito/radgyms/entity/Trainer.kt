package lol.gito.radgyms.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class Trainer : VillagerEntity
{
    override fun getMaxLookYawChange(): Int = 360
    override fun isSilent(): Boolean = true
    override fun isPushable(): Boolean = false
    override fun damage(source: DamageSource, amount: Float): Boolean = false

    constructor(entityType: EntityType<out VillagerEntity>, world: World) : super(entityType, world) {
        this.world = world
        this.setPersistent();
    }

    override fun tickMovement() {
        super.tickMovement()
        velocity = Vec3d.ZERO
    }

    override fun interactMob(player: PlayerEntity?, hand: Hand?): ActionResult {
        if (player is ServerPlayerEntity && world is ServerWorld) {

        }

        return ActionResult.PASS;
    }
}