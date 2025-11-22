/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.ai.config.RCTBattleAIConfig
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import com.gitlab.srcmc.rctapi.api.models.PokemonModel
import com.gitlab.srcmc.rctapi.api.util.JTO
import lol.gito.radgyms.RadGyms.debug
import lol.gito.radgyms.api.dto.Gym
import lol.gito.radgyms.api.enumeration.GymTeamType
import lol.gito.radgyms.api.event.GymEvents
import lol.gito.radgyms.api.event.GymEvents.GENERATE_TEAM
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import lol.gito.radgyms.common.gym.SpeciesManager.generatePokemon
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text.translatable
import net.minecraft.util.math.Vec3d
import com.gitlab.srcmc.rctapi.api.models.TrainerModel as RCTTrainerModel
import lol.gito.radgyms.api.dto.TrainerModel as RGTrainerModel

object GymTemplate {
    lateinit var structure: String
    var relativeExitBlockSpawn: Vec3d = Vec3d.ZERO
    var relativePlayerSpawn: Vec3d = Vec3d.ZERO
    var playerYaw: Float = 0F
    var trainers: List<RGTrainerModel> = mutableListOf()
    var type: String? = null
    var lootTables: List<Gym.Json.LootTableInfo> = mutableListOf()

    fun fromGymDto(player: ServerPlayerEntity, dto: Gym.Json, level: Int, type: String?): GymTemplate {
        structure = dto.template
        lootTables = dto.rewardLootTables

        relativeExitBlockSpawn = Vec3d(
            dto.exitBlockPos[0],
            dto.exitBlockPos[1],
            dto.exitBlockPos[2],
        )

        relativePlayerSpawn = Vec3d(
            dto.playerSpawnRelative.pos[0],
            dto.playerSpawnRelative.pos[1],
            dto.playerSpawnRelative.pos[2],
        )
        playerYaw = dto.playerSpawnRelative.yaw.toFloat()

        trainers = dto.trainers.map {
            trainerDtoToModel(
                it,
                type,
                level,
                player
            )
        }

        return this
    }

    private fun trainerDtoToModel(
        trainer: RGTrainerModel.Json.Trainer,
        type: String?,
        level: Int,
        player: ServerPlayerEntity
    ): RGTrainerModel {
        var battleConfig = RCTBattleAIConfig.Builder()

        when {
            trainer.ai.data?.moveBias != null -> battleConfig = battleConfig
                .withMoveBias(trainer.ai.data.moveBias)

            trainer.ai.data?.statusMoveBias != null -> battleConfig = battleConfig
                .withStatusMoveBias(trainer.ai.data.statusMoveBias)

            trainer.ai.data?.switchBias != null -> battleConfig = battleConfig
                .withSwitchBias(trainer.ai.data.switchBias)

            trainer.ai.data?.itemBias != null -> battleConfig = battleConfig
                .withItemBias(trainer.ai.data.itemBias)

            trainer.ai.data?.maxSelectMargin != null -> battleConfig = battleConfig
                .withMaxSelectMargin(trainer.ai.data.maxSelectMargin)
        }

        val ai = RCTBattleAI(
            battleConfig.build()
        )
        val bag = trainer.bag.map bagMap@{ bagItem ->
            return@bagMap BagItemModel(bagItem.item, bagItem.quantity)
        }

        val team = mutableListOf<PokemonModel>()
        val elementType: String = when (type) {
            "default" -> ElementalTypes.all().random().name.lowercase()
            null -> ElementalTypes.all().random().name.lowercase()
            else -> type
        }
        val possibleFormats = trainer.possibleFormats.toMutableList()

        if (trainer.teamType == GymTeamType.GENERATED) {
            var pokemonCount = 1

            for (mapperLevel in trainer.countPerLevelThreshold.sortedBy { it[0] }) {
                if (level >= mapperLevel[0]) {
                    pokemonCount = mapperLevel[1]
                }
            }

            debug("Derived pokemon count for level $level is $pokemonCount")

            val rawTeam = mutableListOf<PokemonProperties>()
            val generateTeamEvent = GymEvents.GenerateTeamEvent(
                player,
                elementType,
                level,
                trainer.id,
                trainer.leader,
                rawTeam,
                possibleFormats
            )


            (1..pokemonCount).forEach { i ->
                rawTeam.add(generatePokemon(level, elementType))
            }

            GENERATE_TEAM.post(generateTeamEvent) { event ->
                debug("final")
                event.team.forEach { props ->
                    team.add(fillPokemonModelFromPokemon(props))
                }
            }
        } else {
            for (pokeParams in trainer.team!!) {
                val props = PokemonProperties.parse("level=$level $pokeParams")

                team.add(fillPokemonModelFromPokemon(props))
            }
        }

        return RGTrainerModel(
            trainer.id,
            RGTrainerModel.EntityData(
                name = translatable(trainer.name).string,
                relativePosition = Vec3d(
                    trainer.spawnRelative.pos[0],
                    trainer.spawnRelative.pos[1],
                    trainer.spawnRelative.pos[2]
                ),
                yaw = trainer.spawnRelative.yaw.toFloat(),
            ),
            RCTTrainerModel(
                translatable(trainer.name).string,
                JTO.of { ai },
                bag,
                team
            ),
            BattleRules(),
            possibleFormats.random(),
            trainer.leader,
            trainer.requires
        )
    }
}
