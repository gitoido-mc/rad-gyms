package lol.gito.radgyms.event

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.cobblemon.mod.common.platform.events.ServerPlayerEvent
import com.gitlab.srcmc.rctapi.api.battle.BattleManager.TrainerEntityBattleActor
import lol.gito.radgyms.RadGyms
import lol.gito.radgyms.RadGyms.RCT
import lol.gito.radgyms.RadGyms.modIdentifier
import lol.gito.radgyms.block.BlockRegistry
import lol.gito.radgyms.entity.Trainer
import lol.gito.radgyms.gym.GymManager
import lol.gito.radgyms.gym.SpeciesManager.SPECIES_BY_TYPE
import lol.gito.radgyms.gym.SpeciesManager.speciesOfType
import lol.gito.radgyms.world.DimensionManager
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object EventManager {
    fun register() {
        RadGyms.LOGGER.info("Registering event handlers")
        UseBlockCallback.EVENT.register(::onBlockInteract)
        PlayerBlockBreakEvents.BEFORE.register(::onBeforeBlockBreak)
        PlatformEvents.SERVER_STARTING.subscribe(Priority.LOW, ::onServerStart)
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.LOW, ::onPlayerJoin)
        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.LOW, ::onPlayerDisconnect)
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ::onGymBattleWon)
        CobblemonEvents.DATA_SYNCHRONIZED.subscribe(Priority.NORMAL) { _ ->
            RadGyms.LOGGER.info("Cobblemon DATA_SYNCHRONIZED triggered, updating elemental gyms species map")
            onSpeciesUpdate()
        }
        PokemonSpecies.observable.subscribe(Priority.NORMAL) { _ ->
            RadGyms.LOGGER.info("Cobblemon species observable triggered, updating elemental gyms species map")
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
        if (world.registryKey == DimensionManager.RADGYMS_LEVEL_KEY) {
            return if (world.getBlockState(result.blockPos).block != BlockRegistry.GYM_EXIT) {
                ActionResult.FAIL
            } else {
                ActionResult.SUCCESS
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
        if (world.registryKey == DimensionManager.RADGYMS_LEVEL_KEY) {
            return false
        }

        if (state.block == BlockRegistry.GYM_ENTRANCE) {
            var allowBreak = false
            if (player.isSneaking) {
                player.sendMessage(Text.translatable(modIdentifier("message.error.gym_entrance.not-sneaking").toTranslationKey()))
                allowBreak = true
            }
            if (!allowBreak) {
                player.sendMessage(Text.translatable(modIdentifier("message.info.gym_entrance_breaking").toTranslationKey()))
            }
            return allowBreak

        }

        return true
    }

    private fun onServerStart(event: ServerEvent.Starting) {
        val trainerRegistry = RCT.trainerRegistry
        RadGyms.LOGGER.info("initializing RCT trainer mod registry")
        trainerRegistry.init(event.server)
    }

    private fun onPlayerJoin(event: ServerPlayerEvent) {
        RadGyms.LOGGER.info("Adding player ${event.player.name} in RadGyms trainer registry")
        RCT.trainerRegistry.registerPlayer(event.player.uuid.toString(), event.player)
    }

    private fun onPlayerDisconnect(event: ServerPlayerEvent) {
        RadGyms.LOGGER.info("Removing player ${event.player.name} from RCT trainer mod registry")
        RCT.trainerRegistry.unregisterById(event.player.uuid.toString())
    }

    private fun onGymBattleWon(event: BattleVictoryEvent) {
        if (event.wasWildCapture) {
            return
        }

        for (loser in event.losers) {
            val battleActor = loser as TrainerEntityBattleActor
            if (battleActor.type == ActorType.NPC && battleActor.entity is Trainer) {
                val trainer = (battleActor.entity as Trainer)
                trainer.defeated = true
                if (trainer.leader) {
                    val winnerBattleActor = (event.winners.first { it.type == ActorType.PLAYER } as TrainerEntityBattleActor)
                    val player = winnerBattleActor.entity as PlayerEntity

                    GymManager.handleLeaderBattleWon(player, player.world)
                }
             }
        }
    }

    private fun onSpeciesUpdate() {
        for (type in ElementalTypes.all()) {
            SPECIES_BY_TYPE[type.name] = speciesOfType(type)
            RadGyms.LOGGER.info("Added ${SPECIES_BY_TYPE[type.name]?.size} ${type.name} entries to species map")
        }
    }
}