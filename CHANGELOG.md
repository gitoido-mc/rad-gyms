# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [ZeroVer](https://0ver.org).

## [Unreleased]

### <!-- 3 --> Balancing
- Fairy cache rebalance - remove busted form mimikyu
- Electric cache rebalance - add alola bias pichu (fixed that one), pikachu and raichu, remove league cap pikachu
- Removed Eternatus-Eternamax and Necrozma-Ultra from dragon cache
- Removed battle bond greninja from dark cache

### <!-- 4 --> Misc
- Add prek config for changelog gen

## [1.7.3+0.4.4] - 2026-02-25

### <!-- 0 --> Features
- Added particles and sounds for gym entrance block

### <!-- 1 --> Bug Fixes
- Rollback mirrored gradle repo for cobblemon
- Fix translations defaulting to minecraft namespace again
- Add missing fairy type to defaultElementalTypes in [#128](https://github.com/gitoido-mc/rad-gyms/pull/128)
- Refactor type checks to use defaultElementalType list
- Add check for custom trainer texture in [#127](https://github.com/gitoido-mc/rad-gyms/pull/127)
- Fix more GUI translations defaulting to minecraft namespace after TranslationHelper update
- Fix translations defaulting to minecraft namespace after TranslationHelper update
- Ensure i18n provider generates translation keys in proper namespace

### <!-- 4 --> Misc
- Unused library versions cleanup
- Changelog update

## [1.7.3+0.4.3] - 2026-02-20

### <!-- 1 --> Bug Fixes
- More detekt cleanup + msd compat fixes in [#125](https://github.com/gitoido-mc/rad-gyms/pull/125)

## [1.7.3+0.4.2] - 2026-02-18

### <!-- 0 --> Features
- Added missing translations

## [1.7.3+0.4.1] - 2026-02-17

### <!-- 1 --> Bug Fixes
- Change how gym species registry filled to evade mutation of cobblemon species registry

## [1.7.3+0.4.0] - 2026-02-17

### <!-- 0 --> Features
- Implemented custom gym stats in [#119](https://github.com/gitoido-mc/rad-gyms/pull/119)
- Implement gym persistence in [#115](https://github.com/gitoido-mc/rad-gyms/pull/115)

### <!-- 1 --> Bug Fixes
- Added missing mega aspect to ignored species

## [1.7.0+0.4.0-alpha] - 2026-02-04

### <!-- 0 --> Features
- Implement custom fabric datagen in [#114](https://github.com/gitoido-mc/rad-gyms/pull/114)
- SCM versioning via gradle plugin, proper mixin and aw propagation, updated docs build, added pull request workflow in [#113](https://github.com/gitoido-mc/rad-gyms/pull/113)
- New `Fixed` and `Pool` type team generators. Serialization codecs for most of the gym classes. in [#112](https://github.com/gitoido-mc/rad-gyms/pull/112)

### <!-- 4 --> Misc
- Remove artifacts from main

### New Contributors
* @github-actions[bot] made their first contribution

## [1.7.0+0.3.1] - 2026-02-03

### <!-- 0 --> Features
- GH issue templates
- Buildscript for docs
- Configurable pokecache pools
- Moving stuff around + bst selector
- Refined buildscript
- NeoForge + 0.3.1 fixes
- Multiplatform initial push

### <!-- 1 --> Bug Fixes
- GymEnterScreen now uses proper translation string

### <!-- 4 --> Misc
- Alot of stuff
- Tidy up code, resolve deprecated usage and bump dependency versions
- Tidy up resources
- .gitignore update

### New Contributors
* @landonjw made their first contribution

## [1.7.0+0.3.0-stable] - 2025-12-04

### <!-- 0 --> Features
- Disabled worldborder for gym dimension
- License update
- PlayerJoin event now properly handles edge case of player being already present in gym trainers registry
- Server settings are now properly applied to client
- AbstractGymScreen preRender helper function. Fixes missing text labels render because it was rendered on wrong stack "layer"

### <!-- 1 --> Bug Fixes
- Teleportation now respects world borders
- Server state not picking up changes
- Proper gym instance teardown when server stops or crashes
- SpeciesManager not picking up ignoredSpecies/ignoredForms config
- Player entries for particular gym entry were calculated wrongly (agane)
- Player entries for particular gym entry were calculated wrongly
- Caches now roll on only implemented species
- Vec2i helper stack overflow fix
- Updated calls to separated gym manager classes
- Bundle not consumed in creative mode
- Datagen recipe generation
- Caches creative item group was showing incorrect items
- OnLoadModels mixin target fix

### <!-- 4 --> Misc
- Gh-action fix regex cause silly take one thousand minus 7
- Gh-action fix regex cause silly
- Gh-action build on tag
- Gh-action update until I figure out a better way to deliver
- More debug
- Bump Cobblemon to 1.7.1
- Updated datagen
- Entrypoint rename
- Datagen mojmap update
- Gitignore update
- Forgotten gradle version bump

## [1.6.1+0.3.0-beta1] - 2025-11-25

### <!-- 1 --> Bug Fixes
- Added another lowercase transform on clientside just in case

### <!-- 4 --> Misc
- Updated deps in CI publish action and in fabric.mod.json

## [1.6.1+0.3.0-alpha1] - 2025-11-22

### <!-- 0 --> Features
- 1.7 initial support

### <!-- 1 --> Bug Fixes
- Broken build???
- Broken build
- Added loot tables and mineable tag for shard blocks

### <!-- 4 --> Misc
- Datagen
- 1.7.0 build support
- Serialization update and additions
- Gradle and Kotlin version bump
- Suppress unused debug function warnings for now

## [1.6.1+0.2.0-beta6] - 2025-11-12

### <!-- 0 --> Features
- Battle formats
- Gym templates updated with possible battle formats
- Added format field to trainer entity
- Added possibleFormats field to gym declaration DTO
- Custom models for gym keys
- GymBattleEndReason enumeration

### <!-- 1 --> Bug Fixes
- Fixed shardReward config

### <!-- 4 --> Misc
- Removed dead code
- Accessors for RCTBattleAI, persistence groundwork

## [1.6.1+0.2.0-beta5] - 2025-11-05

### <!-- 1 --> Bug Fixes
- Event handling early bailout, fixes RCTMod compa

## [1.6.1+0.2.0-beta4] - 2025-10-29

### <!-- 0 --> Features
- More events
- Custom gym trainer skins
- Refactored cache open handler to event, message fixes, rainbow text extension, extracted shinyRoll function to helper
- Added min, max and deriveAverageGymLevel config params. Fixed LevelSliderWidget.kt initial level hard code. Refactored gym enter open screen workflow to accommodate these changes.
- Nightly builds
- Events
- Persistence initial take
- NBT extensions for BlockPos and Vec3d
- RCTBattleAIAccessor accessor mixin

### <!-- 1 --> Bug Fixes
- Prevent use of cache in offhand (kudos Cobblemon Academy)
- Missing access widener declaration in fabric.mod.json

### <!-- 4 --> Misc
- Code cleanup

## [1.6.1+0.2.0-beta3] - 2025-09-19

### <!-- 1 --> Bug Fixes
- Fix(dep+npc)+chore(docs): Update RCT Api version. Fix doubling battle init. Added player in battle check for somehow stuck battles

## [1.6.1+0.2.0-beta2] - 2025-08-26

### <!-- 1 --> Bug Fixes
- Lower fabric loader dependency version
- Fixed gym exit block interaction

## [1.6.1+0.2.0-beta1] - 2025-08-16

### <!-- 0 --> Features
- Removal of owo-lib and big refactor

### <!-- 1 --> Bug Fixes
- Fixed aether mod maven repo
- I18n + separate gym exit screen texture
- I18n
- Invalid fire entrance variation structure name fix

### <!-- 4 --> Misc
- Steel entrance structure center variation fix
- .gitignore: datagen folder
- Readme update
- Version bump

## [1.6.1+0.2.0-alpha3] - 2025-07-21

### <!-- 0 --> Features
- Move starters to rare caches with lower weight, remove starter evolutions for cache rewards

### <!-- 1 --> Bug Fixes
- Cache poke names not fetching on client side when playing on server

### <!-- 4 --> Misc
- Version bump
- Update changelog
- Broken badges

## [1.6.1+0.2.0-alpha2] - 2025-07-18

### <!-- 0 --> Features
- Typed entrance loot tables

### <!-- 4 --> Misc
- Update changelog

## [1.6.1+0.2.0-alpha1] - 2025-07-16

### <!-- 0 --> Features
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
- Pokecache weight tables
- Build update
- Pokecache item translations and assets
- Cache and shard items, also blocks, minor datagen reorganization

### <!-- 1 --> Bug Fixes
- Localization
- Aether compat mixin

### <!-- 4 --> Misc
- Code style and copyrights
- License headers, stonehenge gym entrance progress
- License headers, formatting
- Updates
- Update changelog
- Intermediate commit to fix changes
- Gradle.properties deps update
- Readme update
- RCTApi battle init update for new version
- BundleItemMixin codestyle

### New Contributors
* @KaptainWutax made their first contribution

## [1.6.1+0.1.13-stable1] - 2025-05-22

### <!-- 4 --> Misc
- CHANGELOG.md workflow

## [1.6.1+0.1.13-stable] - 2025-05-20

### <!-- 0 --> Features
- Aether compat

### <!-- 1 --> Bug Fixes
- Config generation

### <!-- 4 --> Misc
- Update gradle.properties
- Code quality
- Dependency update

## [1.6.1+0.1.12-stable] - 2025-04-20

### <!-- 4 --> Misc
- Github-actions update

## [1.6.1+0.1.11-stable] - 2025-04-05

### <!-- 0 --> Features
- Working debug flag, bundle gym rewards

### <!-- 1 --> Bug Fixes
- Misc localization fixes
- Mixin server bias, misc localization fixes
- Dying in gym breaks its instance

### <!-- 4 --> Misc
- Added changelog generation workflow
- Added CHANGELOG.md
- Additional checks whether gym instance exists

## [1.6.1+0.1.10-stable] - 2025-03-23

### <!-- 1 --> Bug Fixes
- Trainer registration in RCT, entity type fix

## [1.6.1+0.1.9-stable] - 2025-02-28

### <!-- 1 --> Bug Fixes
- Scheduled tasks for gym init

## [1.6.1+0.1.8-stableb] - 2025-02-19

### <!-- 1 --> Bug Fixes
- Preload chunks correctly
- Chunk loading for teleport position

### <!-- 4 --> Misc
- Cleanup imports
- Set trainer entity persistent
- Lower chunk update radius
- Version

## [1.6.1+0.1.8-stable] - 2025-02-18

### <!-- 0 --> Features
- Preload chunk where player will be teleported to. Add 1 second delay before teleport. Use Cobblemon scheduler for executing teleport
- Es_es localization added by FOXz

### <!-- 1 --> Bug Fixes
- Species were mapped to forms incorrectly

## [1.6.1+0.1.7-stable] - 2025-02-17

### <!-- 0 --> Features
- Sparse entrance generation, exclude beaches and oceans from allowed biomes
- Add player party check when using gym entrance

### <!-- 1 --> Bug Fixes
- Fix forms and aspects not being applied correctly
- Use TeleportTarget instead of player.teleport

### New Contributors
* @Brzjomo made their first contribution

## [1.6.1+0.1.6-stable] - 2025-02-15

### <!-- 0 --> Features
- Exit Rope item

### <!-- 1 --> Bug Fixes
- Gym trainer names in cobblemon battlelog
- Cleaning up player gym instances after disconnect
- Exiting the gym after restart
- Gym generation adjustments
- Fallback to default template instead of random template when picking it from gym templates pool
- Creative checks and additional player notifications when Gym key being used by player
- Default values in trainer DTO

### <!-- 4 --> Misc
- Localization update
- Version bump
- Updated rarities for gym keys

## [1.6.1+0.1.4-stable] - 2025-02-13

### <!-- 0 --> Features
- Config file
- Major code cleanup
- Dynamic keys

### <!-- 1 --> Bug Fixes
- OnBattleFainted condition fix

## [1.6.1+0.1.3-stable] - 2025-02-11

### <!-- 0 --> Features
- Boot player from gym on team fainted

### <!-- 4 --> Misc
- Readme and build update

## [1.6.1+0.1.2-stable] - 2025-02-11

### <!-- 0 --> Features
- Modrinth build enbled. Closes #19
- Gym rewards
- Ability to set up manual teams, fixes for gym entrance NBTs and template loading
- Pt_BR localization
- Auto build for CurseForge

### <!-- 1 --> Bug Fixes
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

### <!-- 4 --> Misc
- Version bump

## [1.6.1+0.1.1-beta] - 2025-02-09

### <!-- 0 --> Features
- Gym exit stub
- Gym entrances functionality and nbt in structures, misc reworks
- Player NBT data, gym generation progress

### New Contributors
* @gitoido made their first contribution

[unreleased]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.3+0.4.4...HEAD
[1.7.3+0.4.4]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.3+0.4.3...1.7.3+0.4.4
[1.7.3+0.4.3]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.3+0.4.2...1.7.3+0.4.3
[1.7.3+0.4.2]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.3+0.4.1...1.7.3+0.4.2
[1.7.3+0.4.1]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.3+0.4.0...1.7.3+0.4.1
[1.7.3+0.4.0]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0+0.4.0-alpha...1.7.3+0.4.0
[1.7.0+0.4.0-alpha]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0+0.3.1...1.7.0+0.4.0-alpha
[1.7.0+0.3.1]: https://github.com/gitoido-mc/rad-gyms/compare/1.7.0+0.3.0-stable...1.7.0+0.3.1
[1.7.0+0.3.0-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.3.0-beta1...1.7.0+0.3.0-stable
[1.6.1+0.3.0-beta1]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.3.0-alpha1...1.6.1+0.3.0-beta1
[1.6.1+0.3.0-alpha1]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta6...1.6.1+0.3.0-alpha1
[1.6.1+0.2.0-beta6]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta5...1.6.1+0.2.0-beta6
[1.6.1+0.2.0-beta5]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta4...1.6.1+0.2.0-beta5
[1.6.1+0.2.0-beta4]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta3...1.6.1+0.2.0-beta4
[1.6.1+0.2.0-beta3]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta2...1.6.1+0.2.0-beta3
[1.6.1+0.2.0-beta2]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-beta1...1.6.1+0.2.0-beta2
[1.6.1+0.2.0-beta1]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-alpha3...1.6.1+0.2.0-beta1
[1.6.1+0.2.0-alpha3]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-alpha2...1.6.1+0.2.0-alpha3
[1.6.1+0.2.0-alpha2]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.2.0-alpha1...1.6.1+0.2.0-alpha2
[1.6.1+0.2.0-alpha1]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.13-stable1...1.6.1+0.2.0-alpha1
[1.6.1+0.1.13-stable1]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.13-stable...1.6.1+0.1.13-stable1
[1.6.1+0.1.13-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.12-stable...1.6.1+0.1.13-stable
[1.6.1+0.1.12-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.11-stable...1.6.1+0.1.12-stable
[1.6.1+0.1.11-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.10-stable...1.6.1+0.1.11-stable
[1.6.1+0.1.10-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.9-stable...1.6.1+0.1.10-stable
[1.6.1+0.1.9-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.8-stableb...1.6.1+0.1.9-stable
[1.6.1+0.1.8-stableb]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.8-stable...1.6.1+0.1.8-stableb
[1.6.1+0.1.8-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.7-stable...1.6.1+0.1.8-stable
[1.6.1+0.1.7-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.6-stable...1.6.1+0.1.7-stable
[1.6.1+0.1.6-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.5-stable...1.6.1+0.1.6-stable
[1.6.1+0.1.4-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.3-stable...1.6.1+0.1.4-stable
[1.6.1+0.1.3-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.2-stable...1.6.1+0.1.3-stable
[1.6.1+0.1.2-stable]: https://github.com/gitoido-mc/rad-gyms/compare/1.6.1+0.1.1-beta...1.6.1+0.1.2-stable

<!-- generated by git-cliff -->
