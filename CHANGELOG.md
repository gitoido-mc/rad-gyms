# Changelog

All notable changes to this project will be documented in this file.

The format is *loosely* based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [ZeroVer](https://0ver.org).

## [Unreleased]

### <!-- 0 -->🚀 Features
- NeoForge initial support
- Configurable species pools for Poke Caches
- Trainer's party generator now respects level of the gym and selects pokemons based on BST. That means you will not see Rayquaza in level 10 gyms for example
- New trainer party generator type - `pool`. an addition to existing `fixed` and `generated` types. Allows you to declare a list of pokemons for generator to pick from.

### <!-- 1 -->🐛 Bug Fixes
- GymEnterScreen now uses proper translation string
- Fixed crash caused by disconnecting and connecting multiple times in a row in [#104](https://github.com/gitoido-mc/rad-gyms/pull/104) by @landonjw

### New Contributors
* @landonjw made their first contribution

## [1.7.x+0.3.1-stable] - 2025-12-08

### <!-- 0 -->🐛 Bug Fixes
- Fixed gym leave NPE
- Fixed datagen saving recipes in wrong namespace
- Added checks on server cleanup to prevent shutdown NPE

## [1.7.0+0.3.0-stable] - 2025-12-04

### <!-- 0 -->🚀 Features
- Goodbye Yarn mappings, hello Mojmap!
- License update
- Disabled worldborder for gym dimension

### <!-- 1 -->🐛 Bug Fixes
- Server settings are now properly applied on client
- PlayerJoin event now properly handles edge case of player being already present in gym trainers registry
- Server state not picking up changes
- Proper gym instance teardown when server stops or crashes
- SpeciesManager not picking up ignoredSpecies/ignoredForms config
- Player entries for particular gym entry were calculated wrongly
- Caches now roll on only implemented species
- Vec2i helper stack overflow fix
- Gym reward bundle not consumed in creative mode
- Caches creative item group was showing incorrect items

### <!-- 2 -->🚜 Refactor
- AbstractGymScreen preRender helper function. Fixes missing text labels render because it was rendered on wrong stack "layer"
- Use serverPlayer.server link where possible, at some times global server may be empty
- Use different method to spawn entities to prevent "no entity found" error
- Moved fabric-specific data loader to fabric package
- Misc utility func/classes changes
- Renamed StructureManager to StructurePlacer to better reflect purpose
- More multiplatform logic and missing calls to gym management classes
- Proper implementation of platform agnostic network
- Gym manager separation of concerns
- Fabric client logic separation, proper implementation of multiplatform common class
- Deduplication of GUI screen code
- Structural changes
- Pass uses left for player via net packet when opening gym entrance interface

## [0.3-alpha.1] - 2025-11-22

### <!-- 0 -->🚀 Features
- 1.7 initial support

## [0.2-beta.6] - 2025-11-12

### <!-- 0 -->🚀 Features
- Battle formats
- Gym templates updated with possible battle formats
- Added format field to trainer entity
- Added possibleFormats field to gym declaration DTO
- Custom models for gym keys
- GymBattleEndReason enumeration

### <!-- 1 -->🐛 Bug Fixes
- Fixed shardReward config

### <!-- 10 -->💼 Other
- Merge pull request #83 from gitoido-mc/release/v0.2-beta.6 in [#83](https://github.com/gitoido-mc/rad-gyms/pull/83)

### <!-- 2 -->🚜 Refactor
- TrainerBattleEndHandler now handles reasoning too
- Removed TrainerBattleStart subscription
- Removed TrainerBattleStartHandler (it was for testing)
- Moved evn more more code around
- Cleaned up imports in SpeciesManager
- Added preload chunk tickers to spawn positions between gym and player dimensions
- Refactored PokeCache onUse override to event emit instead of packet send
- Cleaned up GymKey class from client code
- Moved even more code to common namespace
- Moved more code to common namespace
- Moved code to common namespace
- Moved mod event handlers to common namespace
- Added reason parameter to TrainerBattleEnd event
- Remove client code from common source set
- Gym key model scale and position tuning
- Separate source sets for client and server
- Mixin separation of concerns between client and server side
- Client side refactoring

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Removed dead code
- Removed dead code
- Accessors for RCTBattleAI, persistence groundwork

## [0.2-beta.5] - 2025-11-05

### <!-- 1 -->🐛 Bug Fixes
- Event handling early bailout, fixes RCTMod compat

## [0.2-beta.4] - 2025-10-29

### <!-- 0 -->🚀 Features
- More events
- Custom gym trainer skins
- Refactored cache open handler to event, message fixes, rainbow text extension, extracted shinyRoll function to helper
- Added min, max and deriveAverageGymLevel config params. Fixed LevelSliderWidget.kt initial level hard code. Refactored gym enter open screen workflow to accommodate these changes.
- Nightly builds
- Events
- Persistence initial take
- NBT extensions for BlockPos and Vec3d
- RCTBattleAIAccessor accessor mixin

### <!-- 1 -->🐛 Bug Fixes
- Prevent use of cache in offhand (kudos Cobblemon Academy)
- Missing access widener declaration in fabric.mod.json

### <!-- 2 -->🚜 Refactor
- Moved render stuff around
- Moved code around for better segregation, added ServerPlayerUtil.kt for misc extension functions
- Make gym instances spawn at x:0 in designated dimension
- Dead code cleanup
- Gradle shenanigans - client run config deps bump
- Copyrights, moved entity registry to registry package

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Code cleanup

## [0.2-beta.3] - 2025-09-19

### <!-- 1 -->🐛 Bug Fixes
- Fix(dep+npc)+chore(docs): Update RCT Api version. Fix doubling battle init. Added player in battle check for somehow stuck battles

### <!-- 2 -->🚜 Refactor
- Remove compat plugins for now

## [0.2-beta.2] - 2025-08-26

### <!-- 1 -->🐛 Bug Fixes
- Lower fabric loader dependency version
- Fixed gym exit block interaction

## [0.2-beta.1] - 2025-08-16

### <!-- 0 -->🚀 Features
- Removal of owo-lib and big refactor

### <!-- 1 -->🐛 Bug Fixes
- Fixed aether mod maven repo
- I18n + separate gym exit screen texture
- I18n
- Invalid fire entrance variation structure name fix

### <!-- 10 -->💼 Other
- Merge pull request #68 from gitoido-mc/release/v0.2-beta.1 in [#68](https://github.com/gitoido-mc/rad-gyms/pull/68)
- Merge pull request #67 from gitoido-mc/release/v0.2-alpha4 in [#67](https://github.com/gitoido-mc/rad-gyms/pull/67)
- Merge pull request #66 from KaptainWutax/main in [#66](https://github.com/gitoido-mc/rad-gyms/pull/66)
- Fix gyms persistent data not persisting when the player dies or completes the end fight by @KaptainWutax

### <!-- 2 -->🚜 Refactor
- Networking done right
- Separation of concerns, owo-lib dep removal, refactor registries

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Steel entrance structure center variation fix
- .gitignore: datagen folder
- Readme update
- Version bump

## [0.2-alpha.3] - 2025-07-21

### <!-- 0 -->🚀 Features
- Move starters to rare caches with lower weight, remove starter evolutions for cache rewards

### <!-- 1 -->🐛 Bug Fixes
- Cache poke names not fetching on client side when playing on server

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Version bump
- Update changelog
- Broken badges

## [0.2-alpha.2] - 2025-07-18

### <!-- 0 -->🚀 Features
- Typed entrance loot tables

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Update changelog

## [0.2-alpha] - 2025-07-16

### <!-- 0 -->🚀 Features
- Custom gym key models in [#61](https://github.com/gitoido-mc/rad-gyms/pull/61)
- Pokecaches in [#51](https://github.com/gitoido-mc/rad-gyms/pull/51)
- Immersive entrance ruins + localization
- Basic layout for docs site
- 11ty static site init
- RadGyms v0.2 prep
- Recipe compat work
- Lots of cache and geckolib work
- Recipes for caches
- Data and assets update
- Lapis boost amount config
- Admiral integration deps + shadowjar
- Geckolib integration deps
- Pokecaches recipes, allow to use OP Debug Stick to reset gym entrances
- Pokecache I18n, small visual updates on tooltips, UI progress
- Pokecache UI screen init
- Data-driven caches
- Lang update
- Shadowjar + Admiral dependency for quick command creation
- Removed gitbook docs
- Readme update
- Pokecache weight tables
- Build update
- Pokecache item translations and assets
- Cache and shard items, also blocks, minor datagen reorganization

### <!-- 1 -->🐛 Bug Fixes
- Localization
- Aether compat mixin

### <!-- 10 -->💼 Other
- Merge pull request #63 from gitoido-mc/feat/pokemon-caches in [#63](https://github.com/gitoido-mc/rad-gyms/pull/63)
- Merge branch 'feat/pokemon-caches' into main by @KaptainWutax
- Add support for custom gym key models by gym type by @KaptainWutax
- Merge pull request #60 from KaptainWutax/main in [#60](https://github.com/gitoido-mc/rad-gyms/pull/60)
- Set team's pokemon level to gym level if not provided by @KaptainWutax
- Fix structure having no Random during placement by @KaptainWutax

### <!-- 2 -->🚜 Refactor
- DataSaver player mixin slight refactor

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Code style and copyrights
- License headers, stonehenge gym entrance progress
- License headers, formatting
- Updates
- Update changelog
- Intermediate commit to fix changes
- Gradle.properties deps update
- Gradle.properties deps update
- Readme update
- RCTApi battle init update for new version
- BundleItemMixin codestyle

### New Contributors
* @KaptainWutax made their first contribution

## [0.1.13.1-stable] - 2025-05-22

### <!-- 10 -->💼 Other
- Update changelog for v0.1.9 changes
- Fabric.mod.json dependency update
- Update changelog for v0.1.9
- Fabric.mod.json dependency update

### <!-- 7 -->⚙️ Miscellaneous Tasks
- CHANGELOG.md workflow
- CHANGELOG.md workflow
- CHANGELOG.md workflow

## [0.1.13-stable] - 2025-05-20

### <!-- 0 -->🚀 Features
- Aether compat

### <!-- 1 -->🐛 Bug Fixes
- Config generation

### <!-- 10 -->💼 Other
- Update build.yml
- Update changelog for v0.1.9
- Update build.yml
- Merge pull request #57 from gitoido-mc/fix/old-deps-config-gen-aether-compat in [#57](https://github.com/gitoido-mc/rad-gyms/pull/57)
- Update changelog for v0.1.9

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Update gradle.properties
- Code quality
- Dependency update

## [0.1.12-stable] - 2025-04-20

### <!-- 10 -->💼 Other
- Merge pull request #54 from gitoido-mc/hotfix/close-screen-button in [#54](https://github.com/gitoido-mc/rad-gyms/pull/54)
- Closing screen crash fix
- Update changelog for v0.1.9

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Github-actions update
- Github-actions update
- Github-actions update
- Github-actions update
- Github-actions update

## [0.1.11] - 2025-04-05

### <!-- 0 -->🚀 Features
- Working debug flag, bundle gym rewards

### <!-- 1 -->🐛 Bug Fixes
- Misc localization fixes
- Mixin server bias, misc localization fixes
- Dying in gym breaks its instance

### <!-- 10 -->💼 Other
- Merge pull request #49 from gitoido-mc/feat/logging-done-right in [#49](https://github.com/gitoido-mc/rad-gyms/pull/49)
- Merge pull request #48 from gitoido-mc/fix/breaking-gyms-on-player-death in [#48](https://github.com/gitoido-mc/rad-gyms/pull/48)
- Merge pull request #47 from gitoido-mc/fix/breaking-gyms-on-player-death in [#47](https://github.com/gitoido-mc/rad-gyms/pull/47)
- Create FUNDING.yml

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Added changelog generation workflow
- Added CHANGELOG.md
- Additional checks whether gym instance exists

## [0.1.10] - 2025-03-23

### <!-- 1 -->🐛 Bug Fixes
- Trainer registration in RCT, entity type fix

### <!-- 10 -->💼 Other
- Merge pull request #46 from gitoido-mc/fix-entity-not-attached-properly in [#46](https://github.com/gitoido-mc/rad-gyms/pull/46)

## [0.1.9] - 2025-02-28

### <!-- 1 -->🐛 Bug Fixes
- Scheduled tasks for gym init

### <!-- 10 -->💼 Other
- Merge pull request #43 from gitoido-mc/fix-entity-not-attached-properly in [#43](https://github.com/gitoido-mc/rad-gyms/pull/43)
- No commit message

## [0.1.8b-stable] - 2025-02-19

### <!-- 1 -->🐛 Bug Fixes
- Preload chunks correctly
- Chunk loading for teleport position

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Cleanup imports
- Set trainer entity persistent
- Lower chunk update radius
- Version

## [0.1.8-stable] - 2025-02-18

### <!-- 0 -->🚀 Features
- Preload chunk where player will be teleported to. Add 1 second delay before teleport. Use Cobblemon scheduler for executing teleport
- Es_es localization added by FOXz

### <!-- 1 -->🐛 Bug Fixes
- Species were mapped to forms incorrectly

### <!-- 10 -->💼 Other
- Merge pull request #42 from gitoido-mc/feat-preload-teleport-coords-chunk in [#42](https://github.com/gitoido-mc/rad-gyms/pull/42)
- Merge pull request #41 from gitoido-mc/fix-species-aspects-mapping in [#41](https://github.com/gitoido-mc/rad-gyms/pull/41)
- Merge pull request #40 from gitoido-mc/feat-es-es-i18n in [#40](https://github.com/gitoido-mc/rad-gyms/pull/40)

## [0.1.7-stable] - 2025-02-17

### <!-- 0 -->🚀 Features
- Sparse entrance generation, exclude beaches and oceans from allowed biomes
- Add player party check when using gym entrance

### <!-- 1 -->🐛 Bug Fixes
- Fix forms and aspects not being applied correctly
- Use TeleportTarget instead of player.teleport

### <!-- 10 -->💼 Other
- Merge pull request #39 from gitoido-mc/38-form-aspects-doesnt-apply in [#39](https://github.com/gitoido-mc/rad-gyms/pull/39)
- Merge pull request #37 from gitoido-mc/feature-entrance-worldgen in [#37](https://github.com/gitoido-mc/rad-gyms/pull/37)
- Merge pull request #35 from gitoido-mc/feat-add-party-check-gym-entrance in [#35](https://github.com/gitoido-mc/rad-gyms/pull/35)
- Merge pull request #34 from gitoido-mc/fix-player-teleport in [#34](https://github.com/gitoido-mc/rad-gyms/pull/34)
- Merge pull request #32 from Brzjomo/main in [#32](https://github.com/gitoido-mc/rad-gyms/pull/32)
- Chinese translation by @Brzjomo

### New Contributors
* @Brzjomo made their first contribution

## [0.1.6-stable] - 2025-02-15

### <!-- 0 -->🚀 Features
- Exit Rope item

### <!-- 1 -->🐛 Bug Fixes
- Gym trainer names in cobblemon battlelog
- Cleaning up player gym instances after disconnect
- Exiting the gym after restart
- Gym generation adjustments
- Fallback to default template instead of random template when picking it from gym templates pool
- Creative checks and additional player notifications when Gym key being used by player
- Default values in trainer DTO

### <!-- 10 -->💼 Other
- Merge pull request #30 from gitoido-mc/fix-gym-team-dtos in [#30](https://github.com/gitoido-mc/rad-gyms/pull/30)

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Localization update
- Version bump
- Updated rarities for gym keys

## [0.1.5-stable] - 2025-02-13

### <!-- 10 -->💼 Other
- Merge branch 'main' of github.com:gitoido-mc/rad-gyms

## [0.1.4-stable] - 2025-02-13

### <!-- 0 -->🚀 Features
- Config file
- Major code cleanup
- Dynamic keys

### <!-- 1 -->🐛 Bug Fixes
- OnBattleFainted condition fix

### <!-- 10 -->💼 Other
- Merge pull request #29 from gitoido-mc/23-modmenu-integration in [#29](https://github.com/gitoido-mc/rad-gyms/pull/29)
- Merge pull request #27 from gitoido-mc/feature-dynamic-keys in [#27](https://github.com/gitoido-mc/rad-gyms/pull/27)

## [0.1.3-stable] - 2025-02-11

### <!-- 0 -->🚀 Features
- Boot player from gym on team fainted

### <!-- 10 -->💼 Other
- Merge pull request #26 from gitoido-mc/25-implement-single-use-return-item in [#26](https://github.com/gitoido-mc/rad-gyms/pull/26)

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Readme and build update

## [0.1.2-stable] - 2025-02-11

### <!-- 0 -->🚀 Features
- Modrinth build enbled. Closes #19
- Gym rewards
- Ability to set up manual teams, fixes for gym entrance NBTs and template loading
- Pt_BR localization
- Auto build for CurseForge

### <!-- 1 -->🐛 Bug Fixes
- Gym rewards only after beating leader
- Generated cache files ignore
- Fixed gym entrance NBT data persistence and shiny param for generated teams
- Missing key recipe, datagen updates
- Wrong parsing of count_per_level_threshold gym json property
- Added additional checks to onGymBattleWon event handler
- Respect world border when deciding where to spawn player
- Mc-publish action version
- Gym level slider now sets internal level value correctly
- Publish only on release created

## [0.1.1-beta] - 2025-02-09

### <!-- 0 -->🚀 Features
- Gym exit stub
- Gym entrances functionality and nbt in structures, misc reworks
- Player NBT data, gym generation progress

### <!-- 10 -->💼 Other
- Alpha version
- Initial commit

### New Contributors
* @gitoido made their first contribution

[unreleased]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0+0.3.1-stable...HEAD
[1.7.x+0.3.1-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0+0.3.0-stable...1.7.0+0.3.1-stable
[1.7.0+0.3.0-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0/nightly...1.7.0+0.3.0-stable
[0.3-beta.1]: https://github.com/gitoido-mc/rad-gyms/compare/v0.3-alpha.1...v0.3-beta.1
[0.3-alpha.1]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.6...v0.3-alpha.1
[0.2-beta.6]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.5...v0.2-beta.6
[0.2-beta.5]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.4...v0.2-beta.5
[0.2-beta.4]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.3...v0.2-beta.4
[0.2-beta.3]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.2...v0.2-beta.3
[0.2-beta.2]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-beta.1...v0.2-beta.2
[0.2-beta.1]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-alpha.3...v0.2-beta.1
[0.2-alpha.3]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-alpha.2...v0.2-alpha.3
[0.2-alpha.2]: https://github.com/gitoido-mc/rad-gyms/compare/v0.2-alpha...v0.2-alpha.2
[0.2-alpha]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.13.1-stable...v0.2-alpha
[0.1.13.1-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.13-stable...v0.1.13.1-stable
[0.1.13-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.12-stable...v0.1.13-stable
[0.1.12-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.11...v0.1.12-stable
[0.1.11]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.10...v0.1.11
[0.1.10]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.9...v0.1.10
[0.1.9]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.8b-stable...v0.1.9
[0.1.8b-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.8-stable...v0.1.8b-stable
[0.1.8-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.7-stable...v0.1.8-stable
[0.1.7-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.6-stable...v0.1.7-stable
[0.1.6-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.5-stable...v0.1.6-stable
[0.1.5-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.4-stable...v0.1.5-stable
[0.1.4-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.3-stable...v0.1.4-stable
[0.1.3-stable]: https://github.com/gitoido-mc/rad-gyms/compare/v0.1.2-stable...v0.1.3-stable
[0.1.2-stable]: https://github.com/gitoido-mc/rad-gyms/compare/0.1.1-beta...v0.1.2-stable

<!-- generated by git-cliff -->
