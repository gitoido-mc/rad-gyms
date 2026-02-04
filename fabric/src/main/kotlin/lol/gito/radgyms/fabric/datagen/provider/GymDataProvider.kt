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
import lol.gito.radgyms.common.api.dto.Gym
import lol.gito.radgyms.common.api.dto.TrainerModel
import lol.gito.radgyms.common.api.enumeration.GymBattleFormat
import lol.gito.radgyms.common.api.enumeration.GymTeamGeneratorType
import lol.gito.radgyms.common.api.enumeration.GymTeamType
import lol.gito.radgyms.common.api.serialization.RadGymsCodec
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class GymDataProvider(output: FabricDataOutput, lookup: CompletableFuture<HolderLookup.Provider>) :
    FabricCodecDataProvider<Gym.Json>(
        output,
        lookup,
        PackOutput.Target.DATA_PACK,
        "gyms",
        RadGymsCodec.GYM
    ) {

    override fun getName(): String = "Gym data"

    override fun configure(provider: BiConsumer<ResourceLocation, Gym.Json>, lookup: HolderLookup.Provider) {
        ElementalTypes.all().forEach {
            provider.accept(modId(it.showdownId), getDefaultElementalGymDto(it))
        }
    }

    private fun getDefaultElementalGymDto(type: ElementalType): Gym.Json = Gym.Json(
        template = "rad_gyms:gym_interior_default",
        exitBlockPos = Gym.Json.Coords(16.0, 2.0, 16.0),
        playerSpawnRelative = Gym.Json.EntityCoordsAndYaw(
            Gym.Json.Coords(16.5, 2.0, 27.0),
            yaw = -180.0
        ),
        trainers = listOf(
            TrainerModel.Json.Trainer(
                id = "default_trainer_junior",
                name = modId("npc.trainer_junior").toLanguageKey(),
                spawnRelative = Gym.Json.EntityCoordsAndYaw(
                    Gym.Json.Coords(26.5, 2.0, 15.5),
                    yaw = 42.5
                ),
                possibleFormats = listOf(GymBattleFormat.SINGLES),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TrainerModel.Json.Threshold(2, 25),
                    TrainerModel.Json.Threshold(3, 50),
                    TrainerModel.Json.Threshold(4, 100)
                )
            ),
            TrainerModel.Json.Trainer(
                id = "default_trainer_senior",
                name = modId("npc.trainer_senior").toLanguageKey(),
                requires = "default_trainer_junior",
                spawnRelative = Gym.Json.EntityCoordsAndYaw(
                    Gym.Json.Coords(5.5, 2.0, 15.5),
                    yaw = -42.5
                ),
                possibleFormats = listOf(GymBattleFormat.SINGLES, GymBattleFormat.DOUBLES),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TrainerModel.Json.Threshold(3, 25),
                    TrainerModel.Json.Threshold(4, 50),
                    TrainerModel.Json.Threshold(5, 100)
                )
            ),
            TrainerModel.Json.Trainer(
                id = "default_trainer_leader",
                name = modId("npc.leader").toLanguageKey(),
                requires = "default_trainer_senior",
                leader = true,
                spawnRelative = Gym.Json.EntityCoordsAndYaw(
                    Gym.Json.Coords(16.0, 2.0, 6.0),
                    yaw = 0.01
                ),
                possibleFormats = listOf(GymBattleFormat.SINGLES, GymBattleFormat.DOUBLES, GymBattleFormat.TRIPLES),
                possibleElementalTypes = listOf(type),
                teamType = GymTeamType.GENERATED,
                teamGenerator = GymTeamGeneratorType.BST,
                countPerLevelThreshold = listOf(
                    TrainerModel.Json.Threshold(4, 25),
                    TrainerModel.Json.Threshold(5, 50),
                    TrainerModel.Json.Threshold(6, 100)
                )
            ),
        )
    )
}