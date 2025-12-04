/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.radgyms.fabric.datagen.i18n

import lol.gito.radgyms.common.RadGyms.modId
import lol.gito.radgyms.common.registry.RadGymsBlocks
import lol.gito.radgyms.common.registry.RadGymsItemGroups
import lol.gito.radgyms.common.registry.RadGymsItems
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
    private fun provideTranslations(): MutableMap<String, String> {
        return mutableMapOf(
            RadGymsItemGroups.GENERAL_GROUP!!.displayName.string to
                    "Rad Gyms: General",
            RadGymsItemGroups.KEYS_GROUP!!.displayName.string to
                    "Rad Gyms: Keys",
            RadGymsItemGroups.CACHES_GROUP!!.displayName.string to
                    "Rad Gyms: Pokémon caches",
            RadGymsItems.EXIT_ROPE.descriptionId to
                    "Exit rope",
            RadGymsItems.EXIT_ROPE.descriptionId.plus(".tooltip") to
                    "Single-use rope to escape the gym trial",
            RadGymsItems.EXIT_ROPE.descriptionId.plus(".failed") to
                    "It cannot be used here",
            RadGymsItems.GYM_KEY.descriptionId to
                    "Gym key",
            RadGymsItems.GYM_KEY.descriptionId.plus(".attuned") to
                    "Attuned to %s",
            RadGymsItems.CACHE_COMMON.descriptionId to
                    "Common Pokemon cache",
            RadGymsItems.CACHE_UNCOMMON.descriptionId to
                    "Uncommon Pokemon cache",
            RadGymsItems.CACHE_RARE.descriptionId to
                    "Rare Pokemon cache",
            RadGymsItems.CACHE_EPIC.descriptionId to
                    "Epic Pokemon cache",
            RadGymsItems.SHARD_COMMON.descriptionId to
                    "Common cache shard",
            RadGymsItems.SHARD_UNCOMMON.descriptionId to
                    "Uncommon cache shard",
            RadGymsItems.SHARD_RARE.descriptionId to
                    "Rare cache shard",
            RadGymsItems.SHARD_EPIC.descriptionId to
                    "Epic cache shard",
            RadGymsBlocks.GYM_ENTRANCE.descriptionId to
                    "Gym Entrance",
            RadGymsBlocks.GYM_ENTRANCE.descriptionId.plus(".tooltip") to
                    "Type can be changed by using Debug Stick on the block.",
            RadGymsBlocks.GYM_ENTRANCE.descriptionId.plus(".tooltip2") to
                    "All players entry count can be reset by using Debug Stick on it while crouching.",
            RadGymsBlocks.GYM_EXIT.descriptionId to
                    "Gym Exit",
            RadGymsBlocks.SHARD_BLOCK_COMMON.descriptionId to
                    "Common shard block",
            RadGymsBlocks.SHARD_BLOCK_UNCOMMON.descriptionId to
                    "Uncommon shard block",
            RadGymsBlocks.SHARD_BLOCK_RARE.descriptionId to
                    "Rare shard block",
            RadGymsBlocks.SHARD_BLOCK_EPIC.descriptionId to
                    "Epic shard block",
            modId("item.component.type.chaos").toLanguageKey() to
                    "Chaos",
            modId("item.component.gym_type").toLanguageKey() to
                    "Attuned to %s",
            modId("item.component.shiny_boost").toLanguageKey() to
                    "Shiny chance boosted x%s time(s)",
            modId("gym_reward").toLanguageKey("item") to
                    "Level %s %s gym reward cache",
            modId("gui.common.set-gym-level").toLanguageKey() to
                    "Select desirable %s gym level",
            modId("gui.common.set-gym-level-entry").toLanguageKey() to
                    "Select desirable %s gym level (Uses left: %s)",
            modId("gui.common.leave-gym").toLanguageKey() to
                    "You want to leave?",
            modId("gui.common.leave-gym-reward").toLanguageKey() to
                    "Rewards will be lost if leader is not defeated.",
            modId("gui.common.leave").toLanguageKey() to
                    "Leave Gym",
            modId("gui.common.uses_left").toLanguageKey() to
                    "Entrance uses left: %s",
            modId("npc.trainer_junior").toLanguageKey() to
                    "Junior gym trainer",
            modId("npc.trainer_senior").toLanguageKey() to
                    "Senior gym trainer",
            modId("npc.leader").toLanguageKey() to
                    "Gym Leader",
            modId("message.info.gym_entrance_breaking").toLanguageKey() to
                    "Gym entrances do not drop when broken. If you break it, all players will lose access to this entrance",
            modId("message.info.gym_entrance_exhausted").toLanguageKey() to
                    "This gym entrance lost all his energies, look for another one",
            modId("message.info.gym_entrance_party_empty").toLanguageKey() to
                    "Your Pokemon party is not enough. Bring at least 3 Pokemon",
            modId("message.info.gym_entrance_party_fainted").toLanguageKey() to
                    "Your pokemon party requires healing",
            modId("message.info.trainer_required").toLanguageKey() to
                    "Go fight %s before challenging me.",
            modId("message.info.trainer_defeated").toLanguageKey() to
                    "You won! Go challenge next trainer.",
            modId("message.info.leader_defeated").toLanguageKey() to
                    "Congratulations on beating the gym!",
            modId("message.info.gym_failed").toLanguageKey() to
                    "Mysterious forces are teleporting you away from the trial",
            modId("message.info.gym_init").toLanguageKey() to
                    "Mysterious forces are teleporting you to %s trial",
            modId("message.info.gym_complete").toLanguageKey() to
                    "An exit appeared somewhere in gym",
            modId("message.info.command.op_kick").toLanguageKey() to
                    "Mysterious forces are forcibly extracting you out from trial",
            modId("message.info.command.debug_reward").toLanguageKey() to
                    "Generated rewards for gym template %s with poke typing %s with level %s",
            modId("message.info.poke_cache.shiny").toLanguageKey() to
                    "SHINY! ",
            modId("message.info.poke_cache.reward").toLanguageKey() to
                    "You got %s %s from cache!",
            modId("message.error.common.no-response").toLanguageKey() to
                    "Cannot acquire server response, try again",
            modId("message.error.key.not-in-main-hand").toLanguageKey() to
                    "Gym key must be in your main hand",
            modId("message.error.gym_entrance.not-sneaking").toLanguageKey() to
                    "You need to sneak to break gym entrance",
            modId("message.error.command.op_kick").toLanguageKey() to
                    "Cannot kick player %s from gym",
            modId("message.error.command.debug_reward.no_template").toLanguageKey() to
                    "Cannot find template provided",
            modId("message.error.command.debug_reward.no_player").toLanguageKey() to
                    "Command was not executed by player",
            modId("label.rarity.${Rarity.COMMON.name.lowercase()}").toLanguageKey() to "Common",
            modId("label.rarity.${Rarity.UNCOMMON.name.lowercase()}").toLanguageKey() to "Uncommon",
            modId("label.rarity.${Rarity.RARE.name.lowercase()}").toLanguageKey() to "Rare",
            modId("label.rarity.${Rarity.EPIC.name.lowercase()}").toLanguageKey() to "Epic",
        )
    }


    override fun generateTranslations(
        wrapperLookup: HolderLookup.Provider,
        translationBuilder: TranslationBuilder
    ) {
        for (item in provideTranslations()) {
            translationBuilder.add(item.key, item.value)
        }
    }
}
