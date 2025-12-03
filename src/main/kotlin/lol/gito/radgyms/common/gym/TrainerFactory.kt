package lol.gito.radgyms.common.gym

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.gitlab.srcmc.rctapi.api.ai.RCTBattleAI
import com.gitlab.srcmc.rctapi.api.battle.BattleRules
import com.gitlab.srcmc.rctapi.api.models.BagItemModel
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.gym.SpeciesManager.fillPokemonModelFromPokemon
import net.minecraft.network.chat.Component.translatable
import net.minecraft.server.level.ServerPlayer
import com.gitlab.srcmc.rctapi.api.models.TrainerModel as RCTTrainerModel
import lol.gito.radgyms.common.api.dto.TrainerModel as RGTrainerModel

class TrainerFactory(
    private val battleConfigBuilder: BattleConfigBuilder = BattleConfigBuilder()
) {
    fun create(
        trainer: RGTrainerModel.Json.Trainer,
        type: String?,
        level: Int,
        player: ServerPlayer,
        teamGenerator: TeamGenerator
    ): RGTrainerModel {
        val ai = RCTBattleAI(battleConfigBuilder.buildFromDto(trainer.ai))
        val bag = trainer.bag.map { BagItemModel(it.item, it.quantity) }

        val elementType = when (type) {
            "default", null -> com.cobblemon.mod.common.api.types.ElementalTypes.all().random().name.lowercase()
            else -> type
        }

        val possibleFormats = trainer.possibleFormats.toMutableList()

        val team = if (trainer.teamType == GymTeamType.GENERATED) {
            teamGenerator.generate(trainer, level, elementType, player, possibleFormats)
        } else {
            trainer.team!!.map { params ->
                val props = PokemonProperties.parse("level=$level $params")
                fillPokemonModelFromPokemon(props)
            }.toMutableList()
        }

        return RGTrainerModel(
            trainer.id,
            RGTrainerModel.EntityData(
                name = translatable(trainer.name),
                relativePosition = trainer.spawnRelative.pos.toVec3D(),
                yaw = trainer.spawnRelative.yaw.toFloat()
            ),
            RCTTrainerModel(
                translatable(trainer.name).string,
                com.gitlab.srcmc.rctapi.api.util.JTO.of { ai },
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