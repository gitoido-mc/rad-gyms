/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
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
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.cobblemon.mod.common.platform.events.ServerPlayerEvent
import lol.gito.radgyms.common.RadGyms
import lol.gito.radgyms.common.RadGyms.CONFIG
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.api.enumeration.GymBattleEndReason
import lol.gito.radgyms.common.api.event.GymEvents
import lol.gito.radgyms.common.api.event.GymEvents.CACHE_ROLL_POKE
import lol.gito.radgyms.common.api.event.GymEvents.GENERATE_REWARD
import lol.gito.radgyms.common.api.event.GymEvents.GYM_ENTER
import lol.gito.radgyms.common.api.event.GymEvents.GYM_LEAVE
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_BATTLE_END
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_BATTLE_START
import lol.gito.radgyms.common.api.event.GymEvents.TRAINER_INTERACT
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.event.cache.CacheRollPokeHandler
import lol.gito.radgyms.common.event.cache.ShinyCharmCheckHandler
import lol.gito.radgyms.common.event.gyms.*
import lol.gito.radgyms.common.gym.GymInitializer
import lol.gito.radgyms.common.gym.SpeciesManager
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.speciesOfType
import lol.gito.radgyms.common.gym.TrainerFactory
import lol.gito.radgyms.common.gym.TrainerSpawner
import lol.gito.radgyms.common.helper.hasRadGymsTrainers
import lol.gito.radgyms.common.net.server.payload.ServerSettingsS2C
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsCaches
import lol.gito.radgyms.common.registry.RadGymsDimensions
import lol.gito.radgyms.common.registry.RadGymsTemplates
import lol.gito.radgyms.common.world.StructurePlacer
import lol.gito.radgyms.common.world.state.RadGymsState

@Suppress("TooManyFunctions")
object EventManager {
    fun register() {
        PokeBalls.SLATE_BALL.let {
            // do stuff
        }

        debug("Registering event handlers")
        // Minecraft events
        PlatformEvents.SERVER_STARTING.subscribe(Priority.NORMAL, ::onServerStarting)
        PlatformEvents.SERVER_STARTED.subscribe(Priority.NORMAL, ::onServerStarted)
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.NORMAL, ::onPlayerJoin)
        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.HIGHEST, ::onPlayerDisconnect)
        PlatformEvents.RIGHT_CLICK_BLOCK.subscribe(Priority.NORMAL, ::onBlockInteract)

        // Cobblemon events
        PokemonSpecies.observable.subscribe(Priority.LOWEST) { _ ->
            debug("Cobblemon species observable triggered, updating elemental gyms species map")
            onSpeciesUpdate()
        }

        // Cobblemon battle events
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL, ::onBattleStart)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ::onBattleWon)
        CobblemonEvents.BATTLE_FLED.subscribe(Priority.NORMAL, ::onBattleFled)
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, ::onBattleFainted)

        // Mod events
        RadGymsTemplates.observable.subscribe(Priority.NORMAL) { registry ->
            RadGyms.gymInitializer = GymInitializer(
                templateRegistry = registry,
                trainerSpawner = TrainerSpawner,
                structureManager = StructurePlacer,
                trainerFactory = TrainerFactory()
            )
        }

        RadGymsCaches.observable.subscribe(Priority.NORMAL) { registry ->
            SpeciesManager.SPECIES_BY_RARITY = registry.caches.mapKeys { (key, _) ->
                debug("cache key ${key.path}")
                key.path
            }
        }

        GYM_ENTER.subscribe(Priority.LOWEST, ::GymEnterHandler)
        GYM_LEAVE.subscribe(Priority.LOWEST, GymLeaveHandler::execute)

        TRAINER_INTERACT.subscribe(Priority.LOWEST, ::TrainerInteractHandler)
        TRAINER_BATTLE_END.subscribe(Priority.LOWEST, ::TrainerBattleEndHandler)

        GENERATE_REWARD.subscribe(Priority.LOWEST, ::GenerateRewardHandler)

        CACHE_ROLL_POKE.subscribe(Priority.LOWEST, ::ShinyCharmCheckHandler)
        CACHE_ROLL_POKE.subscribe(Priority.LOWEST, ::CacheRollPokeHandler)
    }


    @Suppress("UNUSED_PARAMETER")
    private fun onBlockInteract(event: ServerPlayerEvent.RightClickBlock) {
        if (event.player.level().dimension() == RadGymsDimensions.RADGYMS_LEVEL_KEY) {
            if (CONFIG.debug == true) return
            if (event.player.level().getBlockState(event.pos).block == RadGymsBlocks.GYM_EXIT) return
            event.cancel()
        }
    }

    private fun onServerStarting(event: ServerEvent.Starting) {
        val trainerRegistry = RCT.trainerRegistry
        debug("initializing RCT trainer mod registry")
        trainerRegistry.init(event.server)
    }

    private fun onServerStarted(event: ServerEvent.Started) {
        RadGymsState.getServerState(event.server)
    }

    private fun onPlayerJoin(event: ServerPlayerEvent) {
        debug("Sending server settings to player ${event.player.name}")
        ServerSettingsS2C(
            CONFIG.maxEntranceUses!!,
            CONFIG.shardRewards!!,
            CONFIG.lapisBoostAmount!!,
            CONFIG.ignoredSpecies!!,
            CONFIG.minLevel!!,
            CONFIG.maxLevel!!
        ).sendToPlayer(event.player)

        try {
            debug("Adding player ${event.player.name} to gyms trainer registry")
            RCT.trainerRegistry.registerPlayer(event.player.uuid.toString(), event.player)
            val playerData = RadGymsState.getPlayerState(event.player)
            debug("player gym visits: ${playerData.visits}, has return coords? ${playerData.returnCoords != null}")
        } catch (_: IllegalArgumentException) {
            debug("Player ${event.player.name} is already present in gyms trainer registry, skipping")
        }
    }

    private fun onPlayerDisconnect(event: ServerPlayerEvent) {
        debug("Removing player ${event.player.name} from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(event.player.uuid.toString())

        if (RadGymsState.getGymForPlayer(event.player) == null) return
    }

    private fun onSpeciesUpdate() {
        SPECIES_BY_TYPE.clear()

        ElementalTypes.all().forEach {
            SPECIES_BY_TYPE[it.showdownId] = speciesOfType(it)
            debug("Added ${SPECIES_BY_TYPE[it.showdownId]?.size} ${it.showdownId} entries to species map")
        }
    }

    private fun onBattleStart(event: BattleStartedEvent.Pre) {
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return

        val players = event.battle.players
        val trainers = event.battle.actors
            .filter { it.type == ActorType.NPC && it is AIBattleActor }
            .map { it as AIBattleActor }
            .mapNotNull { RCT.trainerRegistry.getById(it.uuid.toString())?.entity }
            .filterIsInstance<Trainer>()

        TRAINER_BATTLE_START.postThen(
            GymEvents.TrainerBattleStartEvent(players, trainers.map { it }, event.battle),
            { subEvent -> if (subEvent.isCanceled) event.cancel() },
            { _ -> debug("Gym trainer battle started for players: ${players.joinToString(" ") { it.name.string }}") },
        )
    }

    @Suppress("ReturnCount")
    private fun onBattleWon(event: BattleVictoryEvent) {
        // Early bail if it was wild poke battle
        if (event.wasWildCapture)
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

    @Suppress("ReturnCount")
    private fun onBattleFainted(event: BattleFaintedEvent) {
        // Early bail if not gym related
        if (!hasRadGymsTrainers(event)) return

        // Kudos Tim for whiteout mod
        val killed = event.killed
        val entity = killed.entity ?: return
        val owner = entity.owner ?: return
        if (killed.actor.type != ActorType.PLAYER) return
        if (!killed.actor.pokemonList.all { it.health == 0 }) return
        if (owner.isDeadOrDying) return

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
