/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.entity

import lol.gito.radgyms.api.events.ModEvents
import lol.gito.radgyms.common.registry.EventRegistry.TRAINER_INTERACT
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.village.VillagerData
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

class Trainer(entityType: EntityType<out Trainer>, world: World) :
    VillagerEntity(entityType, world) {
    private val gymTrainerIdKey = "gymTrainerId"
    private val trainerIdKey = "trainerId"
    private val requiresKey = "requires"
    private val defeatedKey = "isDefeated"
    private val leaderKey = "isLeader"

    var gymId: String
        get() {
            return this.dataTracker?.get(GYM_ID) ?: "default_trainer"
        }
        set(value) {
            this.dataTracker?.set(GYM_ID, value)
        }

    var trainerId: UUID?
        get() = this.dataTracker?.get(TRAINER_ID)?.getOrNull()
        set(value) {
            this.dataTracker?.set(TRAINER_ID, Optional<UUID>.ofNullable(value))
        }

    var requires: UUID?
        get() = this.dataTracker?.get(REQUIRES)?.getOrNull()
        set(value) {
            this.dataTracker?.set(REQUIRES, Optional<UUID>.ofNullable(value))
        }

    var defeated: Boolean
        get() = this.dataTracker?.get(DEFEATED) ?: false
        set(value) {
            this.dataTracker?.set(DEFEATED, value)
        }

    var leader: Boolean
        get() = this.dataTracker?.get(LEADER) ?: false
        set(value) {
            this.dataTracker?.set(LEADER, value)
        }

    init {
        this.world = world
        this.setPersistent()
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder = createVillagerAttributes()
        val GYM_ID: TrackedData<String> =
            DataTracker.registerData(Trainer::class.java, TrackedDataHandlerRegistry.STRING)
        val TRAINER_ID: TrackedData<Optional<UUID>> =
            DataTracker.registerData(Trainer::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
        val REQUIRES: TrackedData<Optional<UUID>> =
            DataTracker.registerData(Trainer::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
        val DEFEATED: TrackedData<Boolean> =
            DataTracker.registerData(Trainer::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val LEADER: TrackedData<Boolean> =
            DataTracker.registerData(Trainer::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(
            builder
                .add(GYM_ID, "default_trainer")
                .add(TRAINER_ID, Optional<UUID>.ofNullable(null))
                .add(REQUIRES, Optional<UUID>.ofNullable(null))
                .add(DEFEATED, false)
                .add(LEADER, false)
        )
    }

    override fun onTrackedDataSet(data: TrackedData<*>) {
        super.onTrackedDataSet(data)
    }

    override fun setVillagerData(villagerData: VillagerData?) {
        super.setVillagerData(villagerData)
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
        if (!world.isClient) {
            TRAINER_INTERACT.postThen(
                ModEvents.TrainerInteractEvent(player as ServerPlayerEntity, this),
                { event -> return ActionResult.FAIL },
                { event -> return ActionResult.success(world.isClient) },
            )
        }

        return ActionResult.success(world.isClient)
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        nbt.putString(gymTrainerIdKey, gymId)
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
        if (nbt.contains(gymTrainerIdKey)) {
            gymId = nbt.getString(gymTrainerIdKey)
        }
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
