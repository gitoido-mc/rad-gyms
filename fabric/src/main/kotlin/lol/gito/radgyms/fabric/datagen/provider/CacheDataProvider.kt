/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import com.cobblemon.mod.common.api.types.ElementalTypes
import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.api.serialization.MRadGymsCodec
import lol.gito.radgyms.common.cache.CacheDTO
import lol.gito.radgyms.fabric.datagen.provider.caches.BUG_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.DARK_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.DRAGON_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.ELECTRIC_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.FAIRY_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.FIGHTING_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.FIRE_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.FLYING_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.GHOST_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.GRASS_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.GROUND_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.ICE_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.NORMAL_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.POISON_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.PSYCHIC_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.ROCK_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.STEEL_CACHE
import lol.gito.radgyms.fabric.datagen.provider.caches.WATER_CACHE
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class CacheDataProvider(output: FabricDataOutput, lookup: CompletableFuture<HolderLookup.Provider>) :
    FabricCodecDataProvider<CacheDTO>(
        output,
        lookup,
        PackOutput.Target.DATA_PACK,
        "caches",
        MRadGymsCodec.CACHE,
    ) {
    override fun getName(): String = "Pokemon Caches"

    override fun configure(provider: BiConsumer<ResourceLocation, CacheDTO>, lookup: HolderLookup.Provider) {
        val caches = mapOf(
            ElementalTypes.BUG to BUG_CACHE,
            ElementalTypes.DARK to DARK_CACHE,
            ElementalTypes.DRAGON to DRAGON_CACHE,
            ElementalTypes.ELECTRIC to ELECTRIC_CACHE,
            ElementalTypes.FAIRY to FAIRY_CACHE,
            ElementalTypes.FIGHTING to FIGHTING_CACHE,
            ElementalTypes.FIRE to FIRE_CACHE,
            ElementalTypes.FLYING to FLYING_CACHE,
            ElementalTypes.GHOST to GHOST_CACHE,
            ElementalTypes.GRASS to GRASS_CACHE,
            ElementalTypes.GROUND to GROUND_CACHE,
            ElementalTypes.ICE to ICE_CACHE,
            ElementalTypes.NORMAL to NORMAL_CACHE,
            ElementalTypes.POISON to POISON_CACHE,
            ElementalTypes.PSYCHIC to PSYCHIC_CACHE,
            ElementalTypes.ROCK to ROCK_CACHE,
            ElementalTypes.STEEL to STEEL_CACHE,
            ElementalTypes.WATER to WATER_CACHE,
        )

        caches.forEach {
            provider.accept(modId(it.key.showdownId), it.value)
        }
    }
}
