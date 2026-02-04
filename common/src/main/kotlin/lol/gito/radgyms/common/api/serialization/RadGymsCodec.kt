/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.common.api.serialization

import com.cobblemon.mod.common.api.types.ElementalType
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import lol.gito.radgyms.common.api.dto.Gym.Json
import lol.gito.radgyms.common.api.dto.Gym.Json.*
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.AI
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.AI.Config
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.Bag
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.BattleRules
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.Threshold
import lol.gito.radgyms.common.api.dto.TrainerModel.Json.Trainer
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType

object RadGymsCodec {
    @JvmStatic
    val COORDS: Codec<Coords> = RecordCodecBuilder.create {
        it.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Coords::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Coords::y),
            Codec.DOUBLE.fieldOf("z").forGetter(Coords::z)
        ).apply(it, ::Coords)
    }

    @JvmStatic
    val ENTITY_COORDS_YAW: Codec<EntityCoordsAndYaw> = RecordCodecBuilder.create {
        it.group(
            COORDS.fieldOf("pos").forGetter(EntityCoordsAndYaw::pos),
            Codec.DOUBLE.fieldOf("yaw").forGetter(EntityCoordsAndYaw::yaw)
        ).apply(it, ::EntityCoordsAndYaw)
    }

    @JvmStatic
    val LOOT_TABLE_INFO: Codec<LootTableInfo> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("id").forGetter(LootTableInfo::id),
            Codec.INT.fieldOf("min_level").forGetter(LootTableInfo::minLevel),
            Codec.INT.fieldOf("max_level").forGetter(LootTableInfo::maxLevel),
        ).apply(it, ::LootTableInfo)
    }

    @JvmStatic
    val THRESHOLD: Codec<Threshold> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("amount").forGetter(Threshold::amount),
            Codec.INT.fieldOf("until_level").forGetter(Threshold::untilLevel),
        ).apply(it, ::Threshold)
    }

    @JvmStatic
    val BAG: Codec<Bag> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("item").forGetter(Bag::item),
            Codec.INT.fieldOf("quantity").forGetter(Bag::quantity)
        ).apply(it, ::Bag)
    }

    @JvmStatic
    val AI_CONFIG: Codec<Config> = RecordCodecBuilder.create {
        it.group(
            Codec.DOUBLE.lenientOptionalFieldOf("move_bias", null).forGetter(Config::moveBias),
            Codec.DOUBLE.lenientOptionalFieldOf("status_move_bias", null).forGetter(Config::statusMoveBias),
            Codec.DOUBLE.lenientOptionalFieldOf("switch_bias", null).forGetter(Config::switchBias),
            Codec.DOUBLE.lenientOptionalFieldOf("item_bias", null).forGetter(Config::itemBias),
            Codec.DOUBLE.lenientOptionalFieldOf("max_select_margin", null).forGetter(Config::maxSelectMargin),
            Codec.INT.lenientOptionalFieldOf("skill_level", null).forGetter(Config::skillLevel)
        ).apply(it, ::Config)
    }

    @JvmStatic
    val TRAINER_AI: Codec<AI> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("type").forGetter(AI::type),
            AI_CONFIG.optionalFieldOf("data", null).forGetter(AI::data)
        ).apply(it, ::AI)
    }

    @JvmStatic
    val BATTLE_RULES: Codec<BattleRules> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("type").forGetter(BattleRules::maxItemUses)
        ).apply(it, ::BattleRules)
    }

    @JvmStatic
    val TRAINER: Codec<Trainer> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("id").forGetter(Trainer::id),
            Codec.STRING.fieldOf("name").forGetter(Trainer::name),
            ENTITY_COORDS_YAW.fieldOf("spawn_relative").forGetter(Trainer::spawnRelative),
            GymTeamType.CODEC.fieldOf("team_type").forGetter(Trainer::teamType),
            GymTeamGeneratorType.CODEC.fieldOf("team_generator").forGetter(Trainer::teamGenerator),
            Codec.list(ElementalType.BY_STRING_CODEC).fieldOf("possible_elemental_types")
                .forGetter(Trainer::possibleElementalTypes),
            Codec.list(GymBattleFormat.CODEC).fieldOf("possible_formats").forGetter(Trainer::possibleFormats),
            TRAINER_AI.fieldOf("ai").forGetter(Trainer::ai),
            Codec.list(BAG).fieldOf("bag").forGetter(Trainer::bag),
            Codec.list(THRESHOLD).fieldOf("level_thresholds").forGetter(Trainer::countPerLevelThreshold),
            BATTLE_RULES.fieldOf("battle_rules").forGetter(Trainer::battleRules),
            Codec.list(Codec.STRING).fieldOf("team").forGetter(Trainer::team),
            Codec.BOOL.fieldOf("leader").forGetter(Trainer::leader),
            Codec.STRING.lenientOptionalFieldOf("requires", null).forGetter(Trainer::requires)
        ).apply(it, ::Trainer)
    }

    val GYM: Codec<Json> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("interior_template").forGetter(Json::template),
            COORDS.fieldOf("exit_block_pos").forGetter(Json::exitBlockPos),
            ENTITY_COORDS_YAW.fieldOf("player_spawn_relative").forGetter(Json::playerSpawnRelative),
            Codec.list(TRAINER).fieldOf("trainers").forGetter(Json::trainers),
            Codec.list(LOOT_TABLE_INFO).fieldOf("reward_loot_tables").forGetter(Json::rewardLootTables)
        ).apply(it, ::Json)
    }
}