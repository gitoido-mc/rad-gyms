/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.dto.geospatial.Coords
import lol.gito.radgyms.common.api.dto.geospatial.EntityCoordsAndYaw
import lol.gito.radgyms.common.api.dto.gym.GymJson
import lol.gito.radgyms.common.api.dto.trainer.TeamLevelThreshold
import lol.gito.radgyms.common.api.dto.trainer.Trainer
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.api.serialization.MRadGymsCodec
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class GymDataProvider(output: FabricDataOutput, lookup: CompletableFuture<HolderLookup.Provider>) :
    FabricCodecDataProvider<GymJson>(
        output,
        lookup,
        PackOutput.Target.DATA_PACK,
        "gyms",
        MRadGymsCodec.GYM,
    ) {
    override fun getName(): String = "Gym data"

    override fun configure(provider: BiConsumer<ResourceLocation, GymJson>, lookup: HolderLookup.Provider) =
        ElementalTypes.all().forEach {
            provider.accept(modId(it.showdownId), getDefaultElementalGymDto(it))
        }

    @Suppress("LongMethod", "MagicNumber")
    private fun getDefaultElementalGymDto(type: ElementalType): GymJson = GymJson(
        id = type.showdownId,
        template = "rad_gyms:gym_interior_default",
        exitBlockPos = Coords(16.0, 2.0, 16.0),
        playerSpawnRelative = EntityCoordsAndYaw(
            Coords(16.5, 2.0, 27.0),
            yaw = -180.0,
        ),
        trainers = listOf(
            Trainer(
                id = "default_trainer_junior",
                name = modId("npc.trainer_junior").toLanguageKey(),
                spawnRelative = EntityCoordsAndYaw(
                    Coords(26.5, 2.0, 15.5),
                    yaw = 42.5,
                ),
                possibleFormats = listOf(GymBattleFormat.SINGLES),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TeamLevelThreshold(2, 25),
                    TeamLevelThreshold(3, 50),
                    TeamLevelThreshold(4, 100),
                ),
            ),
            Trainer(
                id = "default_trainer_senior",
                name = modId("npc.trainer_senior").toLanguageKey(),
                requires = "default_trainer_junior",
                spawnRelative = EntityCoordsAndYaw(
                    Coords(5.5, 2.0, 15.5),
                    yaw = -42.5,
                ),
                possibleFormats = listOf(GymBattleFormat.SINGLES, GymBattleFormat.DOUBLES),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TeamLevelThreshold(3, 25),
                    TeamLevelThreshold(4, 50),
                    TeamLevelThreshold(5, 100),
                ),
            ),
            Trainer(
                id = "default_trainer_leader",
                name = modId("npc.leader").toLanguageKey(),
                requires = "default_trainer_senior",
                leader = true,
                spawnRelative = EntityCoordsAndYaw(
                    Coords(16.0, 2.0, 6.0),
                    yaw = 0.01,
                ),
                possibleFormats = listOf(
                    GymBattleFormat.SINGLES,
                    GymBattleFormat.DOUBLES,
                    GymBattleFormat.TRIPLES,
                ),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TeamLevelThreshold(4, 25),
                    TeamLevelThreshold(5, 50),
                    TeamLevelThreshold(6, 100),
                ),
            ),
        ),
    )
}
