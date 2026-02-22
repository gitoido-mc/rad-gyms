/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.entity

import lol.gito.radgyms.common.api.dto.trainer.TrainerConfiguration
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_INTERACT
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.jvm.optionals.getOrNull

class Trainer(entityType: EntityType<out Trainer>, level: Level) : Villager(entityType, level) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder = Villager.createAttributes()

        val GYM_ID: EntityDataAccessor<String> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.STRING)
        val FORMAT: EntityDataAccessor<String> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.STRING)
        val TRAINER_ID: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.OPTIONAL_UUID)
        val REQUIRES: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.OPTIONAL_UUID)
        val DEFEATED: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.BOOLEAN)
        val LEADER: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.BOOLEAN)
        val CONFIGURATION: EntityDataAccessor<CompoundTag> =
            SynchedEntityData.defineId(Trainer::class.java, EntityDataSerializers.COMPOUND_TAG)
    }

    private val gymTrainerIdKey = "gymTrainerId"
    private val trainerIdKey = "trainerId"
    private val requiresKey = "requires"
    private val battleFormat = "format"
    private val defeatedKey = "isDefeated"
    private val leaderKey = "isLeader"
    private val configurationKey = "configuration"

    var gymId: String
        get() = entityData.get(GYM_ID) ?: "default_trainer"
        set(value) = entityData.set(GYM_ID, value)

    var format: String
        get() = entityData.get(FORMAT) ?: GymBattleFormat.SINGLES.name.lowercase()
        set(value) = entityData.set(FORMAT, value)

    var trainerId: UUID?
        get() = entityData.get(TRAINER_ID)?.getOrNull()
        set(value) = entityData.set(TRAINER_ID, Optional<UUID>.ofNullable(value))

    var requires: UUID?
        get() = entityData.get(REQUIRES)?.getOrNull()
        set(value) = entityData.set(REQUIRES, Optional<UUID>.ofNullable(value))

    var defeated: Boolean
        get() = entityData.get(DEFEATED) ?: false
        set(value) = entityData.set(DEFEATED, value)

    var leader: Boolean
        get() = entityData.get(LEADER) ?: false
        set(value) = entityData.set(LEADER, value)

    var configuration: TrainerConfiguration
        get() = TrainerConfiguration.fromCompoundTag(entityData.get(CONFIGURATION))
        set(value) = entityData.set(CONFIGURATION, value.toCompoundTag())

    init {
        this.setLevel(level)
        this.setPersistenceRequired()
    }

    override fun getHeadRotSpeed(): Int = 360

    override fun isSilent(): Boolean = true

    override fun isPushable(): Boolean = false

    override fun hurt(source: DamageSource, amount: Float): Boolean = false

    override fun defineSynchedData(builder: SynchedEntityData.Builder) = super.defineSynchedData(
        builder
            .define(GYM_ID, "default_trainer")
            .define(FORMAT, GymBattleFormat.SINGLES.name)
            .define(TRAINER_ID, Optional<UUID>.ofNullable(null))
            .define(REQUIRES, Optional<UUID>.ofNullable(null))
            .define(DEFEATED, false)
            .define(LEADER, false)
            .define(CONFIGURATION, CompoundTag()),
    )

    override fun aiStep() {
        super.aiStep()
        deltaMovement = Vec3.ZERO
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        if (!level().isClientSide) {
            var result: InteractionResult = InteractionResult.FAIL
            TRAINER_INTERACT.postThen(
                event = GymEvents.TrainerInteractEvent(player as ServerPlayer, this),
                ifSucceeded = { _ -> result = InteractionResult.sidedSuccess(level().isClientSide) },
            )

            return result
        }

        return InteractionResult.sidedSuccess(level().isClientSide)
    }

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.putString(gymTrainerIdKey, gymId)
        nbt.putString(battleFormat, format)
        nbt.putBoolean(leaderKey, leader)
        nbt.putBoolean(defeatedKey, defeated)
        nbt.put(configurationKey, configuration.toCompoundTag())

        if (trainerId != null) {
            nbt.putUUID(trainerIdKey, trainerId!!)
        }
        if (requires != null) {
            nbt.putUUID(requiresKey, requires!!)
        }
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        if (nbt.contains(gymTrainerIdKey)) {
            gymId = nbt.getString(gymTrainerIdKey)
        }
        if (nbt.contains(trainerIdKey)) {
            trainerId = nbt.getUUID(trainerIdKey)
        }
        if (nbt.contains(requiresKey)) {
            requires = nbt.getUUID(requiresKey)
        }
        if (nbt.contains(defeatedKey)) {
            defeated = nbt.getBoolean(defeatedKey)
        }
        if (nbt.contains(battleFormat)) {
            format = nbt.getString(battleFormat)
        }
        if (nbt.contains(leaderKey)) {
            leader = nbt.getBoolean(leaderKey)
        }
        if (nbt.contains(configurationKey)) {
            configuration = TrainerConfiguration.fromCompoundTag(nbt.getCompound(configurationKey))
        }
    }
}
