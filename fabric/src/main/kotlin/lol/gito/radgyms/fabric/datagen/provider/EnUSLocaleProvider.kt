/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.provider

import lol.gito.radgyms.common.helper.tlk
import lol.gito.radgyms.common.registry.RadGymsBlocks.GYM_ENTRANCE
import lol.gito.radgyms.common.registry.RadGymsBlocks.GYM_EXIT
import lol.gito.radgyms.common.registry.RadGymsBlocks.SHARD_BLOCK_COMMON
import lol.gito.radgyms.common.registry.RadGymsBlocks.SHARD_BLOCK_EPIC
import lol.gito.radgyms.common.registry.RadGymsBlocks.SHARD_BLOCK_RARE
import lol.gito.radgyms.common.registry.RadGymsBlocks.SHARD_BLOCK_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsItemGroups.CACHES_GROUP
import lol.gito.radgyms.common.registry.RadGymsItemGroups.GENERAL_GROUP
import lol.gito.radgyms.common.registry.RadGymsItemGroups.KEYS_GROUP
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_COMMON
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.CACHE_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsItems.EXIT_ROPE
import lol.gito.radgyms.common.registry.RadGymsItems.GYM_KEY
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_COMMON
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_EPIC
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_RARE
import lol.gito.radgyms.common.registry.RadGymsItems.SHARD_UNCOMMON
import lol.gito.radgyms.common.registry.RadGymsStats.CACHES_OPENED
import lol.gito.radgyms.common.registry.RadGymsStats.ENTRANCES_USED
import lol.gito.radgyms.common.registry.RadGymsStats.GYMS_BEATEN
import lol.gito.radgyms.common.registry.RadGymsStats.GYMS_FAILED
import lol.gito.radgyms.common.registry.RadGymsStats.GYMS_VISITED
import lol.gito.radgyms.common.registry.RadGymsStats.KEYS_USED
import lol.gito.radgyms.common.registry.RadGymsStats.ROPES_USED
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Rarity
import java.util.concurrent.CompletableFuture

class EnUSLocaleProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>
) : FabricLanguageProvider(
    dataOutput,
    "en_us",
    registryLookup
) {
    private fun provideTranslations(): Map<String, String> {
        val translations = mutableMapOf<String, String>()

        translations.putAll(infoTranslations())
        translations.putAll(errorTranslations())
        translations.putAll(guiTranslations())
        translations.putAll(statTranslations())
        translations.putAll(itemTranslations())
        translations.putAll(objectTranslations())

        return translations
    }

    private fun itemTranslations(): Map<String, String> = mapOf(
        EXIT_ROPE.descriptionId to "Exit rope",
        EXIT_ROPE.descriptionId.plus(".tooltip") to "Single-use rope to escape the gym trial",
        EXIT_ROPE.descriptionId.plus(".failed") to "It cannot be used here",
        GYM_KEY.descriptionId to "Gym key",
        GYM_KEY.descriptionId.plus(".attuned") to "Attuned to %s",
        CACHE_COMMON.descriptionId to "Common Pokemon cache",
        CACHE_UNCOMMON.descriptionId to "Uncommon Pokemon cache",
        CACHE_RARE.descriptionId to "Rare Pokemon cache",
        CACHE_EPIC.descriptionId to "Epic Pokemon cache",
        SHARD_COMMON.descriptionId to "Common cache shard",
        SHARD_UNCOMMON.descriptionId to "Uncommon cache shard",
        SHARD_RARE.descriptionId to "Rare cache shard",
        SHARD_EPIC.descriptionId to "Epic cache shard",
        tlk("item.component.type.chaos") to "Chaos",
        tlk("item.component.gym_type") to "Attuned to %s",
        tlk("item.component.shiny_boost") to "Shiny chance boosted x%s time(s)",
        tlk("item", "gym_reward") to "Level %s %s gym reward cache",
    )

    private fun infoTranslations(): Map<String, String> = mapOf(
        tlk("message.info.gym_entrance_breaking") to "Gym entrances do not drop when broken."
            .plus(" If you break it, all players will lose access to this entrance"),
        tlk("message.info.gym_entrance_exhausted") to
            "This gym entrance lost all his energies, look for another one",
        tlk("message.info.gym_entrance_party_empty") to
            "Your Pokemon party is not enough. Bring at least 3 Pokemon",
        tlk("message.info.gym_entrance_party_fainted") to "Your pokemon party requires healing",
        tlk("message.info.trainer_required") to "Go fight %s before challenging me.",
        tlk("message.info.trainer_defeated") to "You won! Go challenge next trainer.",
        tlk("message.info.leader_defeated") to "Congratulations on beating the gym!",
        tlk("message.info.gym_failed") to "Mysterious forces are teleporting you away from the trial",
        tlk("message.info.gym_init") to "Mysterious forces are teleporting you to %s trial",
        tlk("message.info.gym_complete") to "An exit appeared somewhere in gym",
        tlk("message.info.command.config_reloaded") to "Rad Gyms config reloaded!",
        tlk("message.info.command.op_kick") to "Mysterious forces are forcibly extracting you out from trial",
        tlk("message.info.command.debug_reward") to
            "Generated rewards for gym template %s with poke typing %s with level %s",
        tlk("message.info.poke_cache.shiny") to "SHINY ",
        tlk("message.info.poke_cache.reward") to "You got %s %s from cache!",
    )

    private fun errorTranslations(): Map<String, String> = mapOf(
        tlk("message.error.common.no-response") to "Cannot acquire server response, try again",
        tlk("message.error.key.not-in-main-hand") to "Gym key must be in your main hand",
        tlk("message.error.gym_entrance.not-sneaking") to "You need to sneak to break gym entrance",
        tlk("message.error.command.op_kick") to "Cannot kick player %s from gym",
        tlk("message.error.command.kick.wrong_dim") to "Target is not in the gym dimension",
        tlk("message.error.command.kick.no_player") to "There is no such player currently online",
        tlk("message.error.command.debug_reward.no_template") to "Cannot find template provided",
        tlk("message.error.command.debug_reward.no_player") to "Command was not executed by player"
    )

    private fun objectTranslations(): Map<String, String> = mapOf(
        GYM_ENTRANCE.descriptionId to "Gym Entrance",
        GYM_ENTRANCE.descriptionId.plus(".tooltip") to "Type can be changed by using Debug Stick on the block.",
        GYM_ENTRANCE.descriptionId.plus(".tooltip2") to
            "All players entry count can be reset by using Debug Stick on it while crouching.",
        GYM_EXIT.descriptionId to "Gym Exit",
        SHARD_BLOCK_COMMON.descriptionId to "Common shard block",
        SHARD_BLOCK_UNCOMMON.descriptionId to "Uncommon shard block",
        SHARD_BLOCK_RARE.descriptionId to "Rare shard block",
        SHARD_BLOCK_EPIC.descriptionId to "Epic shard block",
        tlk("npc.trainer_junior") to "Junior gym trainer",
        tlk("npc.trainer_senior") to "Senior gym trainer",
        tlk("npc.leader") to "Gym Leader",
    )

    private fun guiTranslations(): Map<String, String> = mapOf(
        GENERAL_GROUP!!.displayName.string to "Rad Gyms: General",
        KEYS_GROUP!!.displayName.string to "Rad Gyms: Keys",
        CACHES_GROUP!!.displayName.string to "Rad Gyms: Pokémon caches",
        tlk("gui.common.set-gym-level") to "Select desirable %s gym level",
        tlk("gui.common.set-gym-level-entry") to "Select desirable %s gym level (Uses left: %s)",
        tlk("gui.common.leave-gym") to "You want to leave?",
        tlk("gui.common.leave-gym-reward") to "Rewards will be lost if leader is not defeated.",
        tlk("gui.common.leave") to "Leave Gym",
        tlk("gui.common.uses_left") to "Entrance uses left: %s",
        tlk("biome", "gym_biome") to "Gym Trials",
        tlk("label.rarity.${Rarity.COMMON.name.lowercase()}") to "Common",
        tlk("label.rarity.${Rarity.UNCOMMON.name.lowercase()}") to "Uncommon",
        tlk("label.rarity.${Rarity.RARE.name.lowercase()}") to "Rare",
        tlk("label.rarity.${Rarity.EPIC.name.lowercase()}") to "Epic"
    )

    private fun statTranslations(): Map<String, String> = mapOf(
        tlk("stat", GYMS_VISITED.resourceLocation) to "Gyms visited",
        tlk("stat", GYMS_BEATEN.resourceLocation) to "Gyms beaten",
        tlk("stat", GYMS_FAILED.resourceLocation) to "Gyms failed",
        tlk("stat", KEYS_USED.resourceLocation) to "Gym keys used",
        tlk("stat", ENTRANCES_USED.resourceLocation) to "Gym entrances used",
        tlk("stat", ROPES_USED.resourceLocation) to "Gym escape ropes used",
        tlk("stat", CACHES_OPENED.resourceLocation) to "Poke caches opened",
    )

    override fun generateTranslations(
        wrapperLookup: HolderLookup.Provider,
        translationBuilder: TranslationBuilder
    ) {
        for (item in provideTranslations()) {
            translationBuilder.add(item.key, item.value)
        }
    }
}
