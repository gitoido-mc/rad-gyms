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
import com.mojang.serialization.codecs.UnboundedMapCodec
import lol.gito.radgyms.common.api.dto.*
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.cache.CacheDTO
import net.minecraft.world.item.Rarity

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
    val THRESHOLD: Codec<TeamLevelThreshold> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("amount").forGetter(TeamLevelThreshold::amount),
            Codec.INT.fieldOf("until_level").forGetter(TeamLevelThreshold::untilLevel),
        ).apply(it, ::TeamLevelThreshold)
    }

    @JvmStatic
    val BAG: Codec<TrainerBag> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("item").forGetter(TrainerBag::item),
            Codec.INT.fieldOf("quantity").forGetter(TrainerBag::quantity)
        ).apply(it, ::TrainerBag)
    }

    @JvmStatic
    val AI_CONFIG: Codec<BattleAIConfig> = RecordCodecBuilder.create {
        it.group(
            Codec.DOUBLE.lenientOptionalFieldOf("move_bias", null).forGetter(BattleAIConfig::moveBias),
            Codec.DOUBLE.lenientOptionalFieldOf("status_move_bias", null).forGetter(BattleAIConfig::statusMoveBias),
            Codec.DOUBLE.lenientOptionalFieldOf("switch_bias", null).forGetter(BattleAIConfig::switchBias),
            Codec.DOUBLE.lenientOptionalFieldOf("item_bias", null).forGetter(BattleAIConfig::itemBias),
            Codec.DOUBLE.lenientOptionalFieldOf("max_select_margin", null).forGetter(BattleAIConfig::maxSelectMargin),
            Codec.INT.lenientOptionalFieldOf("skill_level", null).forGetter(BattleAIConfig::skillLevel)
        ).apply(it, ::BattleAIConfig)
    }

    @JvmStatic
    val TRAINER_AI: Codec<BattleAI> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("type").forGetter(BattleAI::type),
            AI_CONFIG.optionalFieldOf("data", null).forGetter(BattleAI::data)
        ).apply(it, ::BattleAI)
    }

    @JvmStatic
    val BATTLE_RULES: Codec<BattleRules> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("max_item_uses").forGetter(BattleRules::maxItemUses)
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
            Codec.list(Codec.STRING).lenientOptionalFieldOf("team", null).forGetter(Trainer::team),
            Codec.BOOL.fieldOf("leader").forGetter(Trainer::leader),
            Codec.STRING.lenientOptionalFieldOf("requires", null).forGetter(Trainer::requires)
        ).apply(it, ::Trainer)
    }

    @JvmStatic
    val GYM: Codec<GymJson> = RecordCodecBuilder.create {
        it.group(
            Codec.STRING.fieldOf("interior_template").forGetter(GymJson::template),
            COORDS.fieldOf("exit_block_pos").forGetter(GymJson::exitBlockPos),
            ENTITY_COORDS_YAW.fieldOf("player_spawn_relative").forGetter(GymJson::playerSpawnRelative),
            Codec.list(TRAINER).fieldOf("trainers").forGetter(GymJson::trainers),
            Codec.list(LOOT_TABLE_INFO).fieldOf("reward_loot_tables").forGetter(GymJson::rewardLootTables)
        ).apply(it, ::GymJson)
    }

    @JvmStatic
    val CACHE_POOL: UnboundedMapCodec<Rarity, Map<String, Int>> = Codec.unboundedMap(
        Rarity.CODEC, Codec.unboundedMap(Codec.STRING, Codec.INT)
    )

    @JvmStatic
    val CACHE: Codec<CacheDTO> = RecordCodecBuilder.create {
        it.group(CACHE_POOL.fieldOf("pools").forGetter(CacheDTO::pools)).apply(it, ::CacheDTO)
    }
}
