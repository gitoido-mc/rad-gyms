# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [ZeroVer](https://0ver.org).

## [Unreleased]

### <!-- 2 -->🚜 Refactor
- Changed serialization to gson and refactored config load

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Updated gradle, deps and removed kotlinx.serialization

## [1.7.3+0.4.4] - 2026-02-25

### <!-- 0 -->🚀 Features
- Added particles and sounds for gym entrance block

### <!-- 1 -->🐛 Bug Fixes
- Rollback mirrored gradle repo for cobblemon
- Fix translations defaulting to minecraft namespace again
- Add missing fairy type to defaultElementalTypes
- Refactor type checks to use defaultElementalType list
- Add check for custom trainer texture
- Fix more GUI translations defaulting to minecraft namespace after TranslationHelper update
- Fix translations defaulting to minecraft namespace after TranslationHelper update
- Ensure i18n provider generates translation keys in proper namespace

### <!-- 2 -->🚜 Refactor
- Detekt checks, code flow optimizations, reintroduced snapshot switch

### <!-- 5 -->🎨 Styling
- Detekt checks
- Cleanup dead code and rename assembleProperties to setLevel in generic team generator

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Unused library versions cleanup
- Changelog update
- Remove dangling docs project

## [1.7.3+0.4.3] - 2026-02-20

### <!-- 1 -->🐛 Bug Fixes
- More detekt cleanup + msd compat fixes

### <!-- 5 -->🎨 Styling
- Detekt checks
- More detekt cleanup
- Dead code cleanup
- Update copyrights, more code cleanup
- Fixed code smell according to detekt in loader-specific proj, reran datagen to ensure parity
- Updated detekt rules

## [1.7.3+0.4.2] - 2026-02-18

### <!-- 0 -->🚀 Features
- Added missing translations

### <!-- 2 -->🚜 Refactor
- Changed custom stats registration on world init

### <!-- 5 -->🎨 Styling
- Cleaned dead code in config file

## [1.7.3+0.4.1] - 2026-02-17

### <!-- 1 -->🐛 Bug Fixes
- Change how gym species registry filled to evade mutation of cobblemon species registry

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Reintroduce dependencies into buildscript

## [1.7.3+0.4.0] - 2026-02-17

### <!-- 0 -->🚀 Features
- Implemented custom gym stats
- Implement gym persistence

### <!-- 1 -->🐛 Bug Fixes
- Added missing mega aspect to ignored species

### <!-- 10 -->💼 Other
- 0.4.0
- Repair commands

### <!-- 2 -->🚜 Refactor
- Small optimizations
- Reintroduce datagen resources

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Update publishing script once again to ensure multiplatform delivery
- Add download artifacts task
- Update publish script
- Update build file to use dynamic versioning for jars
- Add JDK setup step in CodeQL workflow
- Update CodeQL workflow configuration
- Update gitignore, fine-tune scm versioning, update pull_request workflow

## [1.7.0+0.4.0-alpha] - 2026-02-04

### <!-- 0 -->🚀 Features
- Implement custom fabric datagen
- SCM versioning via gradle plugin, proper mixin and aw propagation, updated docs build, added pull request workflow
- New `Fixed` and `Pool` type team generators. Serialization codecs for most of the gym classes.

### <!-- 2 -->🚜 Refactor
- DX - annotate logger methods, modId helper and RadGymsState companion object methods as @JvmStatic

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Remove artifacts from main

## [1.7.0+0.3.1] - 2026-02-03

### <!-- 0 -->🚀 Features
- GH issue templates
- Buildscript for docs
- Configurable pokecache pools
- Moving stuff around + bst selector
- Refined buildscript
- NeoForge + 0.3.1 fixes
- Multiplatform initial push

### <!-- 1 -->🐛 Bug Fixes
- GymEnterScreen now uses proper translation string

### <!-- 10 -->💼 Other
- Port fix up to 0.4.x

### <!-- 2 -->🚜 Refactor
- Humanize level thresholds in gym definitions
- Alot of updates

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Alot of stuff
- Tidy up code, resolve deprecated usage and bump dependency versions
- Tidy up resources
- .gitignore update

## [1.7.0+0.3.0-stable] - 2025-12-04

### <!-- 0 -->🚀 Features
- Disabled worldborder for gym dimension
- License update
- PlayerJoin event now properly handles edge case of player being already present in gym trainers registry
- Server settings are now properly applied to client
- AbstractGymScreen preRender helper function. Fixes missing text labels render because it was rendered on wrong stack "layer"

### <!-- 1 -->🐛 Bug Fixes
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

### <!-- 10 -->💼 Other
- 0.3-stable
- Update README.md

### <!-- 2 -->🚜 Refactor
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
- Architectury preparation
- Pass uses left for player via net packet when opening gym entrance interface
- Mojmap

### <!-- 7 -->⚙️ Miscellaneous Tasks
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

### <!-- 1 -->🐛 Bug Fixes
- Added another lowercase transform on clientside just in case

### <!-- 2 -->🚜 Refactor
- Removed RCTApi mixin, because it was fixed on their side

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Updated deps in CI publish action and in fabric.mod.json

## [1.6.1+0.3.0-alpha1] - 2025-11-22

### <!-- 0 -->🚀 Features
- 1.7 initial support

### <!-- 1 -->🐛 Bug Fixes
- Broken build???
- Broken build
- Added loot tables and mineable tag for shard blocks

### <!-- 10 -->💼 Other
- 1.7.0 initial support

### <!-- 2 -->🚜 Refactor
- Moved alot of code around
- Moved code around

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Datagen
- 1.7.0 build support
- Serialization update and additions
- Gradle and Kotlin version bump
- Suppress unused debug function warnings for now

## [1.6.1+0.2.0-beta6] - 2025-11-12

### <!-- 0 -->🚀 Features
- Battle formats
- Gym templates updated with possible battle formats
- Added format field to trainer entity
- Added possibleFormats field to gym declaration DTO
- Custom models for gym keys
- GymBattleEndReason enumeration

### <!-- 1 -->🐛 Bug Fixes
- Fixed shardReward config

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
- Accessors for RCTBattleAI, persistence groundwork

## [1.6.1+0.2.0-beta5] - 2025-11-05

### <!-- 1 -->🐛 Bug Fixes
- Event handling early bailout, fixes RCTMod compa

## [1.6.1+0.2.0-beta4] - 2025-10-29

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

## [1.6.1+0.2.0-beta3] - 2025-09-19

### <!-- 1 -->🐛 Bug Fixes
- Fix(dep+npc)+chore(docs): Update RCT Api version. Fix doubling battle init. Added player in battle check for somehow stuck battles

### <!-- 2 -->🚜 Refactor
- Remove compat plugins for now

## [1.6.1+0.2.0-beta2] - 2025-08-26

### <!-- 1 -->🐛 Bug Fixes
- Lower fabric loader dependency version
- Fixed gym exit block interaction

## [1.6.1+0.2.0-beta1] - 2025-08-16

### <!-- 0 -->🚀 Features
- Removal of owo-lib and big refactor

### <!-- 1 -->🐛 Bug Fixes
- Fixed aether mod maven repo
- I18n + separate gym exit screen texture
- I18n
- Invalid fire entrance variation structure name fix

### <!-- 10 -->💼 Other
- Fix gyms persistent data not persisting when the player dies or completes the end fight

### <!-- 2 -->🚜 Refactor
- Networking done right
- Separation of concerns, owo-lib dep removal, refactor registries

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Steel entrance structure center variation fix
- .gitignore: datagen folder
- Readme update
- Version bump

## [1.6.1+0.2.0-alpha3] - 2025-07-21

### <!-- 0 -->🚀 Features
- Move starters to rare caches with lower weight, remove starter evolutions for cache rewards

### <!-- 1 -->🐛 Bug Fixes
- Cache poke names not fetching on client side when playing on server

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Version bump
- Update changelog
- Broken badges

## [1.6.1+0.2.0-alpha2] - 2025-07-18

### <!-- 0 -->🚀 Features
- Typed entrance loot tables

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Update changelog

## [1.6.1+0.2.0-alpha1] - 2025-07-16

### <!-- 0 -->🚀 Features
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

### <!-- 1 -->🐛 Bug Fixes
- Localization
- Aether compat mixin

### <!-- 10 -->💼 Other
- Add support for custom gym key models by gym type
- Set team's pokemon level to gym level if not provided
- Fix structure having no Random during placement

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
- Readme update
- RCTApi battle init update for new version
- BundleItemMixin codestyle

## [1.6.1+0.1.13-stable1] - 2025-05-22

### <!-- 10 -->💼 Other
- Update changelog for v0.1.9 changes
- Fabric.mod.json dependency update
- Update changelog for v0.1.9

### <!-- 7 -->⚙️ Miscellaneous Tasks
- CHANGELOG.md workflow

## [1.6.1+0.1.13-stable] - 2025-05-20

### <!-- 0 -->🚀 Features
- Aether compat

### <!-- 1 -->🐛 Bug Fixes
- Config generation

### <!-- 10 -->💼 Other
- Update build.yml
- Update changelog for v0.1.9

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Update gradle.properties
- Code quality
- Dependency update

## [1.6.1+0.1.12-stable] - 2025-04-20

### <!-- 10 -->💼 Other
- Closing screen crash fix
- Update changelog for v0.1.9

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Github-actions update

## [1.6.1+0.1.11-stable] - 2025-04-05

### <!-- 0 -->🚀 Features
- Working debug flag, bundle gym rewards

### <!-- 1 -->🐛 Bug Fixes
- Misc localization fixes
- Mixin server bias, misc localization fixes
- Dying in gym breaks its instance

### <!-- 10 -->💼 Other
- Create FUNDING.yml

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Added changelog generation workflow
- Added CHANGELOG.md
- Additional checks whether gym instance exists

## [1.6.1+0.1.10-stable] - 2025-03-23

### <!-- 1 -->🐛 Bug Fixes
- Trainer registration in RCT, entity type fix

## [1.6.1+0.1.9-stable] - 2025-02-28

### <!-- 1 -->🐛 Bug Fixes
- Scheduled tasks for gym init

### <!-- 10 -->💼 Other
- No commit message

## [1.6.1+0.1.8-stableb] - 2025-02-19

### <!-- 1 -->🐛 Bug Fixes
- Preload chunks correctly
- Chunk loading for teleport position

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Cleanup imports
- Set trainer entity persistent
- Lower chunk update radius
- Version

## [1.6.1+0.1.8-stable] - 2025-02-18

### <!-- 0 -->🚀 Features
- Preload chunk where player will be teleported to. Add 1 second delay before teleport. Use Cobblemon scheduler for executing teleport
- Es_es localization added by FOXz

### <!-- 1 -->🐛 Bug Fixes
- Species were mapped to forms incorrectly

## [1.6.1+0.1.7-stable] - 2025-02-17

### <!-- 0 -->🚀 Features
- Sparse entrance generation, exclude beaches and oceans from allowed biomes
- Add player party check when using gym entrance

### <!-- 1 -->🐛 Bug Fixes
- Fix forms and aspects not being applied correctly
- Use TeleportTarget instead of player.teleport

### <!-- 10 -->💼 Other
- Chinese translation

## [1.6.1+0.1.6-stable] - 2025-02-15

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

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Localization update
- Version bump
- Updated rarities for gym keys

## [1.6.1+0.1.5-stable] - 2025-02-13

## [1.6.1+0.1.4-stable] - 2025-02-13

### <!-- 0 -->🚀 Features
- Config file
- Major code cleanup
- Dynamic keys

### <!-- 1 -->🐛 Bug Fixes
- OnBattleFainted condition fix

## [1.6.1+0.1.3-stable] - 2025-02-11

### <!-- 0 -->🚀 Features
- Boot player from gym on team fainted

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Readme and build update

## [1.6.1+0.1.2-stable] - 2025-02-11

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

### <!-- 7 -->⚙️ Miscellaneous Tasks
- Version bump

## [1.6.1+0.1.1-beta] - 2025-02-09

### <!-- 0 -->🚀 Features
- Gym exit stub
- Gym entrances functionality and nbt in structures, misc reworks
- Player NBT data, gym generation progress

### <!-- 10 -->💼 Other
- Beta 0.1.1
- 0.1.0-alpha
- Alpha version
- Initial commit

[unreleased]: https://github.com///compare/1.7.3+0.4.4...HEAD
[1.7.3+0.4.4]: https://github.com///compare/1.7.3+0.4.3...1.7.3+0.4.4
[1.7.3+0.4.3]: https://github.com///compare/1.7.3+0.4.2...1.7.3+0.4.3
[1.7.3+0.4.2]: https://github.com///compare/1.7.3+0.4.1...1.7.3+0.4.2
[1.7.3+0.4.1]: https://github.com///compare/1.7.3+0.4.0...1.7.3+0.4.1
[1.7.3+0.4.0]: https://github.com///compare/1.7.0+0.4.0-alpha...1.7.3+0.4.0
[1.7.0+0.4.0-alpha]: https://github.com///compare/1.7.0+0.3.1...1.7.0+0.4.0-alpha
[1.7.0+0.3.1]: https://github.com///compare/1.7.0+0.3.0-stable...1.7.0+0.3.1
[1.7.0+0.3.0-stable]: https://github.com///compare/1.6.1+0.3.0-beta1...1.7.0+0.3.0-stable
[1.6.1+0.3.0-beta1]: https://github.com///compare/1.6.1+0.3.0-alpha1...1.6.1+0.3.0-beta1
[1.6.1+0.3.0-alpha1]: https://github.com///compare/1.6.1+0.2.0-beta6...1.6.1+0.3.0-alpha1
[1.6.1+0.2.0-beta6]: https://github.com///compare/1.6.1+0.2.0-beta5...1.6.1+0.2.0-beta6
[1.6.1+0.2.0-beta5]: https://github.com///compare/1.6.1+0.2.0-beta4...1.6.1+0.2.0-beta5
[1.6.1+0.2.0-beta4]: https://github.com///compare/1.6.1+0.2.0-beta3...1.6.1+0.2.0-beta4
[1.6.1+0.2.0-beta3]: https://github.com///compare/1.6.1+0.2.0-beta2...1.6.1+0.2.0-beta3
[1.6.1+0.2.0-beta2]: https://github.com///compare/1.6.1+0.2.0-beta1...1.6.1+0.2.0-beta2
[1.6.1+0.2.0-beta1]: https://github.com///compare/1.6.1+0.2.0-alpha3...1.6.1+0.2.0-beta1
[1.6.1+0.2.0-alpha3]: https://github.com///compare/1.6.1+0.2.0-alpha2...1.6.1+0.2.0-alpha3
[1.6.1+0.2.0-alpha2]: https://github.com///compare/1.6.1+0.2.0-alpha1...1.6.1+0.2.0-alpha2
[1.6.1+0.2.0-alpha1]: https://github.com///compare/1.6.1+0.1.13-stable1...1.6.1+0.2.0-alpha1
[1.6.1+0.1.13-stable1]: https://github.com///compare/1.6.1+0.1.13-stable...1.6.1+0.1.13-stable1
[1.6.1+0.1.13-stable]: https://github.com///compare/1.6.1+0.1.12-stable...1.6.1+0.1.13-stable
[1.6.1+0.1.12-stable]: https://github.com///compare/1.6.1+0.1.11-stable...1.6.1+0.1.12-stable
[1.6.1+0.1.11-stable]: https://github.com///compare/1.6.1+0.1.10-stable...1.6.1+0.1.11-stable
[1.6.1+0.1.10-stable]: https://github.com///compare/1.6.1+0.1.9-stable...1.6.1+0.1.10-stable
[1.6.1+0.1.9-stable]: https://github.com///compare/1.6.1+0.1.8-stableb...1.6.1+0.1.9-stable
[1.6.1+0.1.8-stableb]: https://github.com///compare/1.6.1+0.1.8-stable...1.6.1+0.1.8-stableb
[1.6.1+0.1.8-stable]: https://github.com///compare/1.6.1+0.1.7-stable...1.6.1+0.1.8-stable
[1.6.1+0.1.7-stable]: https://github.com///compare/1.6.1+0.1.6-stable...1.6.1+0.1.7-stable
[1.6.1+0.1.6-stable]: https://github.com///compare/1.6.1+0.1.5-stable...1.6.1+0.1.6-stable
[1.6.1+0.1.5-stable]: https://github.com///compare/1.6.1+0.1.4-stable...1.6.1+0.1.5-stable
[1.6.1+0.1.4-stable]: https://github.com///compare/1.6.1+0.1.3-stable...1.6.1+0.1.4-stable
[1.6.1+0.1.3-stable]: https://github.com///compare/1.6.1+0.1.2-stable...1.6.1+0.1.3-stable
[1.6.1+0.1.2-stable]: https://github.com///compare/1.6.1+0.1.1-beta...1.6.1+0.1.2-stable

<!-- generated by git-cliff -->
