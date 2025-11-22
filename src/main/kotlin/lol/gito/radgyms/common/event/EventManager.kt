/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent
import com.cobblemon.mod.common.api.events.battles.BattleStartedEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.cobblemon.mod.common.platform.events.ServerPlayerEvent
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.RadGyms.modId
import lol.gito.radgyms.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.api.event.GymEvents.GYM_ENTER
import lol.gito.radgyms.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.api.event.GymEvents.TRAINER_BATTLE_END
import lol.gito.radgyms.api.event.GymEvents.TRAINER_BATTLE_START
import lol.gito.radgyms.api.event.GymEvents.TRAINER_INTERACT
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.event.cache.CacheRollPokeHandler
import lol.gito.radgyms.common.event.cache.ShinyCharmCheckHandler
import lol.gito.radgyms.common.event.gyms.*
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.speciesOfType
import lol.gito.radgyms.common.registry.BlockRegistry
import lol.gito.radgyms.common.registry.DimensionRegistry
import lol.gito.radgyms.common.state.RadGymsState
import lol.gito.radgyms.common.util.hasRadGymsTrainers
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object EventManager {
    fun register() {
        debug("Registering event handlers")
        // Minecraft events
        UseBlockCallback.EVENT.register(::onBlockInteract)
        PlayerBlockBreakEvents.BEFORE.register(::onBeforeBlockBreak)
        PlatformEvents.SERVER_STARTING.subscribe(Priority.NORMAL, ::onServerStart)
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.NORMAL, ::onPlayerJoin)
//        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.HIGHEST, ::onPlayerDisconnect)

        // Cobblemon events
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, ::onBattleStart)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ::onBattleWon)
        CobblemonEvents.BATTLE_FLED.subscribe(Priority.NORMAL, ::onBattleFled)
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, ::onBattleFainted)
        PokemonSpecies.observable.subscribe(Priority.NORMAL) { _ ->
            debug("Cobblemon species observable triggered, updating elemental gyms species map")
            onSpeciesUpdate()
        }

        // Mod events
        GYM_ENTER.subscribe(Priority.LOWEST, ::GymEnterHandler)
        GYM_LEAVE.subscribe(Priority.LOWEST, ::GymLeaveHandler)

        TRAINER_INTERACT.subscribe(Priority.LOWEST, ::TrainerInteractHandler)
        TRAINER_BATTLE_END.subscribe(Priority.LOWEST, ::TrainerBattleEndHandler)

        GENERATE_REWARD.subscribe(Priority.LOWEST, ::GenerateRewardHandler)

        CACHE_ROLL_POKE.subscribe(Priority.LOWEST, ::ShinyCharmCheckHandler)
        CACHE_ROLL_POKE.subscribe(Priority.LOWEST, ::CacheRollPokeHandler)
    }


    @Suppress("UNUSED_PARAMETER")
    private fun onBlockInteract(
        playerEntity: PlayerEntity,
        world: World,
        hand: Hand,
        result: BlockHitResult,
    ): ActionResult {
        if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            if (RadGyms.CONFIG.debug == true) {
                return ActionResult.PASS
            }

            if (world.isClient) return ActionResult.PASS
            return when (world.getBlockState(result.blockPos).block == BlockRegistry.GYM_EXIT) {
                true -> ActionResult.PASS
                false -> ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onBeforeBlockBreak(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        entity: BlockEntity?
    ): Boolean {
        var allowBreak = true

        if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            if (RadGyms.CONFIG.debug == true) return true

            allowBreak = false
        }

        if (state.block == BlockRegistry.GYM_ENTRANCE) {
            if (!player.isSneaking) {
                player.sendMessage(translatable(modId("message.info.gym_entrance_breaking").toTranslationKey()))
                player.sendMessage(translatable(modId("message.error.gym_entrance.not-sneaking").toTranslationKey()))
                allowBreak = false
            } else {
                allowBreak = true
            }
        }

        return allowBreak
    }

    private fun onServerStart(event: ServerEvent.Starting) {
        val trainerRegistry = RCT.trainerRegistry
        debug("initializing RCT trainer mod registry")
        trainerRegistry.init(event.server)
    }

    private fun onPlayerJoin(event: ServerPlayerEvent) {
        debug("Adding player ${event.player.name} in RadGyms trainer registry")
        RCT.trainerRegistry.registerPlayer(event.player.uuid.toString(), event.player)
        val playerData = RadGymsState.getPlayerState(event.player)
        debug("player gym visits: ${playerData.visits}, has return coords? ${playerData.returnCoords != null}")
    }

    private fun onPlayerDisconnect(event: ServerPlayerEvent) {
        debug("Removing player ${event.player.name} from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(event.player.uuid.toString())

        if (event.player.world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            GymManager.spawnExitBlock(RadGymsState.getGymForPlayer(event.player)!!)
            GymManager.destructGym(event.player, removeCoords = false)
        }
    }

    private fun onSpeciesUpdate() {
        SPECIES_BY_TYPE.clear()
        ElementalTypes.all().forEach {
            SPECIES_BY_TYPE[it.name] = speciesOfType(it)
            debug("Added ${SPECIES_BY_TYPE[it.name]?.size} ${it.name} entries to species map")
        }
    }

    private fun onBattleStart(event: BattleStartedEvent.Pre) {
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return

        val players = event.battle.players
        val trainers = event.battle.actors
            .filter { it -> it.type == ActorType.NPC && it is AIBattleActor }
            .map { it as AIBattleActor }
            .mapNotNull { RCT.trainerRegistry.getById(it.uuid.toString())?.entity }
            .filter { it is Trainer }

        TRAINER_BATTLE_START.postThen(
            GymEvents.TrainerBattleStartEvent(players, trainers.map { it as Trainer }, event.battle),
            { subEvent -> if (subEvent.isCanceled) event.cancel() },
            { subEvent -> debug("Gym trainer battle started for players: ${players.joinToString(" ") { it.name.string }}") },
        )
    }

    private fun onBattleWon(event: BattleVictoryEvent) {
        // Early bail if it was wild poke battle
        if (event.wasWildCapture) return
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return
        if (event.losers.none { it.type == ActorType.NPC }) return
        if (event.winners.none { it.type == ActorType.PLAYER }) return

        TRAINER_BATTLE_END.emit(
            GymEvents.TrainerBattleEndEvent(
                GymBattleEndReason.BATTLE_WON,
                event.winners,
                event.losers,
                event.battle
            )
        )
    }

    private fun onBattleFled(event: BattleFledEvent) {
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return

        TRAINER_BATTLE_END.emit(
            GymEvents.TrainerBattleEndEvent(
                GymBattleEndReason.BATTLE_FLED,
                event.battle.winners,
                event.battle.losers,
                event.battle
            )
        )
    }

    private fun onBattleFainted(event: BattleFaintedEvent) {
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return

        // Kudos Tim for whiteout mod
        val killed = event.killed
        val entity = killed.entity ?: return
        val owner = entity.owner ?: return
        if (killed.actor.type != ActorType.PLAYER) return
        if (!killed.actor.pokemonList.all { it.health == 0 }) return
        if (owner.isDead) return


        TRAINER_BATTLE_END.emit(
            GymEvents.TrainerBattleEndEvent(
                GymBattleEndReason.BATTLE_LOST,
                event.battle.winners,
                event.battle.losers,
                event.battle
            )
        )
    }
}
