/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.api.event

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.api.reactive.EventObservable
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.api.enumeration.GuiScreenCloseChoice
import lol.gito.radgyms.common.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymLeaveReason
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymTemplate
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

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
        var selectedLevel: Int,
        var usesLeft: Int?
    )

    // Caches
    data class CacheRollPokeEvent(
        val player: ServerPlayer,
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
        val id: ResourceLocation,
    )

    data class GenerateRewardEvent(
        val player: ServerPlayer,
        val template: GymTemplate,
        val level: Int,
        val type: String,
        val rewards: MutableList<ItemStack> = mutableListOf()
    )

    data class GenerateTeamEvent(
        val player: ServerPlayer,
        val type: String,
        val level: Int,
        val trainerId: String,
        val isLeader: Boolean,
        val team: MutableList<PokemonProperties>,
        val possibleFormats: MutableList<GymBattleFormat>
    )

    data class GymEnterEvent(
        val player: ServerPlayer,
        val gym: Gym,
        val type: String,
        val level: Int,
        val usedKey: Boolean
    )

    data class GymLeaveEvent(
        val reason: GymLeaveReason,
        val player: ServerPlayer,
        val completed: Boolean?,
        val usedRope: Boolean?,
        val gym: Gym? = null,
        val type: String? = null,
        val level: Int? = null
    )

    // Trainer interaction

    data class TrainerBattleEndEvent(
        val reason: GymBattleEndReason,
        val winners: List<BattleActor>,
        val losers: List<BattleActor>,
        val battle: PokemonBattle
    )

    data class TrainerBattleStartEvent(
        val players: List<ServerPlayer>,
        val trainers: List<Trainer>,
        val battle: PokemonBattle
    ) : Cancelable()

    data class TrainerInteractEvent(
        val player: ServerPlayer,
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