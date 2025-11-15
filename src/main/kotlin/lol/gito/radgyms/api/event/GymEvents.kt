/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.api.event

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.api.reactive.EventObservable
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymInstance
import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos

@Suppress("unused")
object GymEvents {
    // GUI
    data class GymEnterScreenCloseEvent(
        val choice: GuiScreenCloseChoice,
        val key: Boolean,
        val level: Int,
        val type: String? = null,
        val pos: BlockPos? = null
    )

    data class GymEnterScreenOpenEvent(
        val pos: BlockPos?,
        val key: Boolean,
        var type: String,
        var minLevel: Int,
        var maxLevel: Int,
        var selectedLevel: Int
    )

    // Caches
    data class CacheRollPokeEvent(
        val player: ServerPlayerEntity,
        var poke: Pokemon,
        var type: String,
        var rarity: Rarity,
        var shinyBoost: Int
    )

    // Gyms
    data class GymLeaveScreenCloseEvent(
        val choice: GuiScreenCloseChoice
    )

    data class GymLeaveScreenOpenEvent(
        val id: Identifier
    )

    data class GenerateRewardEvent(
        val player: ServerPlayerEntity,
        val template: GymTemplate,
        val level: Int,
        val type: String,
        val rewards: MutableList<ItemStack> = mutableListOf()
    )

    data class GenerateTeamEvent(
        val player: ServerPlayerEntity,
        val type: String,
        val level: Int,
        val trainerId: String,
        val isLeader: Boolean,
        val team: MutableList<PokemonProperties>
    )

    data class GymEnterEvent(
        val player: ServerPlayerEntity,
        val gym: GymInstance,
        val type: String,
        val level: Int,
        val usedKey: Boolean
    )

    data class GymLeaveEvent(
        val reason: GymLeaveReason,
        val player: ServerPlayerEntity,
        val gym: GymInstance?,
        val type: String?,
        val level: Int?,
        val completed: Boolean?,
        val usedRope: Boolean?
    )

    // Trainer interaction

    data class TrainerBattleEndEvent(
        val reason: GymBattleEndReason,
        val winners: List<BattleActor>,
        val losers: List<BattleActor>,
        val battle: PokemonBattle
    )

    data class TrainerBattleStartEvent(
        val players: List<ServerPlayerEntity>,
        val trainers: List<Trainer>,
        val battle: PokemonBattle
    ) : Cancelable()

    data class TrainerInteractEvent(
        val player: ServerPlayerEntity,
        val trainer: Trainer
    ) : Cancelable()

    @JvmField
    val CACHE_ROLL_POKE = EventObservable<CacheRollPokeEvent>()

    @JvmField
    val GYM_ENTER = EventObservable<GymEnterEvent>()

    @JvmField
    val GYM_LEAVE = EventObservable<GymLeaveEvent>()

    @JvmField
    val GENERATE_REWARD = EventObservable<GenerateRewardEvent>()

    @JvmField
    val GENERATE_TEAM = EventObservable<GenerateTeamEvent>()

    @JvmField
    val TRAINER_INTERACT = CancelableObservable<TrainerInteractEvent>()

    @JvmField
    val TRAINER_BATTLE_START = CancelableObservable<TrainerBattleStartEvent>()

    @JvmField
    val TRAINER_BATTLE_END = EventObservable<TrainerBattleEndEvent>()

    @JvmField
    val ENTER_SCREEN_OPEN = EventObservable<GymEnterScreenOpenEvent>()

    @JvmField
    val ENTER_SCREEN_CLOSE = EventObservable<GymEnterScreenCloseEvent>()

    @JvmField
    val LEAVE_SCREEN_OPEN = EventObservable<GymLeaveScreenOpenEvent>()

    @JvmField
    val LEAVE_SCREEN_CLOSE = EventObservable<GymLeaveScreenCloseEvent>()
}