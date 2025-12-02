/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

package lol.gito.radgyms.fabric.datagen.loot

//class GymLootDataProvider(
//    output: FabricDataOutput,
//    registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
//) : SimpleFabricLootTableProvider(output, registryLookup, LootContextTypes.GENERIC) {
//    override fun accept(lootTableBiConsumer: BiConsumer<RegistryKey<LootTable>, LootTable.Builder>) {
//        lootTableBiConsumer.accept(
//            RadGymsLootTables.SHARED_LOOT_TABLE,
//            generateSharedDefaultLootTable()
//        )
//        lootTableBiConsumer.accept(
//            RadGymsLootTables.COMMON_LOOT_TABLE,
//            generateCommonLootTable()
//        )
//        lootTableBiConsumer.accept(
//            RadGymsLootTables.UNCOMMON_LOOT_TABLE,
//            generateUncommonLootTable()
//        )
//        lootTableBiConsumer.accept(
//            RadGymsLootTables.RARE_LOOT_TABLE,
//            generateRareLootTable()
//        )
//        lootTableBiConsumer.accept(
//            RadGymsLootTables.EPIC_LOOT_TABLE,
//            generateEpicLootTable()
//        )
//    }
//
//    private fun generateSharedDefaultLootTable() = LootTable.Builder()
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(0f, 2f))
//                .bonusRolls(ConstantLootNumberProvider.create(.25f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.ANY_HELD_ITEM))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.EVOLUTION_ITEMS))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(0f, 2f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TUMBLESTONES))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.RESTORES))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.VITAMINS))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(ItemEntry.builder(Items.LAPIS_LAZULI))
//        )
//
//    private fun generateCommonLootTable() = LootTable.Builder()
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(0f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_1_POKE_BALLS))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_1_POKE_BALL_MATERIALS))
//                .with(ItemEntry.builder(CobblemonItems.POTION))
//                .with(ItemEntry.builder(CobblemonItems.ETHER))
//                .with(ItemEntry.builder(CobblemonItems.REVIVE))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(0f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(ItemEntry.builder(RadGymsItems.SHARD_COMMON))
//        )
//
//    private fun generateUncommonLootTable() = LootTable.Builder()
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_2_POKE_BALLS))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_2_POKE_BALL_MATERIALS))
//                .with(ItemEntry.builder(CobblemonItems.SUPER_POTION))
//                .with(ItemEntry.builder(CobblemonItems.MAX_ETHER))
//                .with(ItemEntry.builder(CobblemonItems.REVIVE))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(ItemEntry.builder(RadGymsItems.SHARD_UNCOMMON))
//        )
//
//    private fun generateRareLootTable() = LootTable.Builder()
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_3_POKE_BALLS))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_3_POKE_BALL_MATERIALS))
//                .with(ItemEntry.builder(CobblemonItems.HYPER_POTION))
//                .with(ItemEntry.builder(CobblemonItems.ELIXIR))
//                .with(ItemEntry.builder(CobblemonItems.REVIVE))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(ItemEntry.builder(RadGymsItems.SHARD_RARE))
//        )
//
//    private fun generateEpicLootTable() = LootTable.Builder()
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_4_POKE_BALLS))
//                .with(TagEntry.expandBuilder(CobblemonItemTags.TIER_4_POKE_BALL_MATERIALS))
//                .with(ItemEntry.builder(CobblemonItems.MAX_POTION))
//                .with(ItemEntry.builder(CobblemonItems.MAX_ELIXIR))
//                .with(ItemEntry.builder(CobblemonItems.MAX_REVIVE))
//        )
//        .pool(
//            LootPool.builder()
//                .rolls(UniformLootNumberProvider.create(1f, 5f))
//                .bonusRolls(ConstantLootNumberProvider.create(.5f))
//                .with(ItemEntry.builder(RadGymsItems.SHARD_EPIC))
//        )
//}
