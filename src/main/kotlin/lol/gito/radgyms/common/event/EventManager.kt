/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.event

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.cobblemon.mod.common.platform.events.ServerPlayerEvent
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.common.RadGyms.RCT
import lol.gito.radgyms.common.RadGyms.debug
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.entity.Trainer
import lol.gito.radgyms.common.gym.GymManager
import lol.gito.radgyms.common.gym.GymManager.PLAYER_GYMS
import lol.gito.radgyms.common.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.common.gym.SpeciesManager.speciesOfType
import lol.gito.radgyms.common.registry.BlockRegistry
import lol.gito.radgyms.common.registry.DimensionRegistry
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object EventManager {
    fun register() {
        debug("Registering event handlers")
        UseBlockCallback.EVENT.register(::onBlockInteract)
        PlayerBlockBreakEvents.BEFORE.register(::onBeforeBlockBreak)
        PlatformEvents.SERVER_STARTING.subscribe(Priority.LOW, ::onServerStart)
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.LOW, ::onPlayerJoin)
        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.HIGHEST, ::onPlayerDisconnect)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ::onGymBattleWon)
        CobblemonEvents.BATTLE_FLED.subscribe(Priority.LOWEST, ::onGymBattleFled)
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.LOWEST, ::onGymBattleFainted)

        PokemonSpecies.observable.subscribe(Priority.LOWEST) { _ ->
            debug("Cobblemon species observable triggered, updating elemental gyms species map")
            onSpeciesUpdate()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onBlockInteract(
        playerEntity: PlayerEntity,
        world: World,
        hand: Hand,
        result: BlockHitResult,
    ): ActionResult {
        if (world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
            return if (playerEntity.mainHandStack.item == BlockRegistry.GYM_EXIT.asItem()) {
                ActionResult.CONSUME
            } else {
                when (world.getBlockState(result.blockPos).block != BlockRegistry.GYM_EXIT) {
                    true -> ActionResult.FAIL
                    false -> ActionResult.PASS
                }
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
//        CHANNEL.serverHandle(event.player).send(CachePokeSync())
    }

    private fun onPlayerDisconnect(event: ServerPlayerEvent) {
        debug("Removing player ${event.player.name} from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(event.player.uuid.toString())

        if (event.player.world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY) {
//            CHANNEL.serverHandle(event.player).send(GymLeave(teleport = true))
        }

        PLAYER_GYMS[event.player.uuid]?.npcList?.forEach {
            debug("Removing trainer ${it.second} from registry and discarding associated entity")
            val trainer = RCT.trainerRegistry.getById(it.first.toString())
            trainer.entity.discard()
            RCT.trainerRegistry.unregisterById(it.first.toString())
        }
    }

    private fun onGymBattleWon(event: BattleVictoryEvent) {
        if (event.wasWildCapture) {
            return
        }

        if (event.losers.none { it.type == ActorType.NPC }) {
            return
        }

        if (event.winners.none { it.type == ActorType.PLAYER }) {
            return
        }

        val winnerBattleActor = (event.winners.first { it.type == ActorType.PLAYER } as PlayerBattleActor)
        val player = winnerBattleActor.entity as ServerPlayerEntity
        event.losers.forEach { loser ->
            if (loser.type == ActorType.NPC && loser is TrainerEntityBattleActor && loser.entity is Trainer) {
                (loser.entity as Trainer).let { trainer ->
                    trainer.defeated = true
                    if (trainer.leader) {
                        val gym = PLAYER_GYMS[player.uuid]!!
                        GymManager.handleLeaderBattleWon(player)
                        GymManager.handleLootDistribution(player, gym.template, gym.level, gym.type)
                    }
                }
            }
        }
    }

    private fun onSpeciesUpdate() {
        SPECIES_BY_TYPE.clear()
        ElementalTypes.all().forEach {
            SPECIES_BY_TYPE[it.name] = speciesOfType(it)
            debug("Added ${SPECIES_BY_TYPE[it.name]?.size} ${it.name} entries to species map")
        }
    }

    private fun onGymBattleFled(event: BattleFledEvent) {
        val loser = event.player
        if (loser.type != ActorType.PLAYER) return

        event.battle.players
            .filter { it.world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY }
            .forEach { player ->
//                CHANNEL.serverHandle(player).send(GymLeave(teleport = true))
            }
    }

    private fun onGymBattleFainted(event: BattleFaintedEvent) {
        val killed = event.killed
        val entity = killed.entity ?: return
        val owner = entity.owner ?: return
        if (killed.actor.type != ActorType.PLAYER) return
        if (!killed.actor.pokemonList.all { it.health == 0 }) return
        if (owner.isDead) return

        event.battle.players
            .filter { it.world.registryKey == DimensionRegistry.RADGYMS_LEVEL_KEY }
            .forEach { player ->
//                CHANNEL.serverHandle(player).send(GymLeave(teleport = true))
            }
    }
}
