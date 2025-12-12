/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow") version ("9.2.2")
}

repositories {
    maven("https://maven.fabricmc.net/")
}

val shadowCommon: Configuration = configurations.maybeCreate("shadowCommon")
val generatedResources: File = project(":common").file("src/generated").absoluteFile

architectury {
    platformSetupLoomIde()
    fabric {
        fabricApi {
            configureDataGeneration {
                client = true
                modId = rootProject.property("mod_id") as String
                outputDirectory = generatedResources
            }
        }
    }
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    @Suppress("UnstableApiUsage")
    mixin {
        useLegacyMixinAp = true
        defaultRefmapName = "mixins.${rootProject.property("mod_id")}.refmap.json"
    }

    runs {
        getByName("client") {
            programArgs(
                "--username=Gitoido",
                "--uuid=23131d78-9edb-48a4-902a-e22e572e9f2b"
            )
        }
        getByName("server") {
            runDir = "run-server"
        }

        getByName("datagen") {
            runDir = "run-datagen"
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric_kotlin_version")}")

    // Helpers
    modImplementation("dev.architectury:architectury-fabric:${rootProject.property("architectury_api_version")}")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${rootProject.property("cobblemon_version")}") { isTransitive = false }

    // Common code
    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))
    shadowCommon(project(":common", configuration = "transformProductionFabric"))

    // Platform specific
    modImplementation("dev.architectury:architectury-fabric:$rootProject.architectury_api_version")
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${rootProject.property("rctapi_fabric_version")}")

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${property("aether_version")}-fabric")
}

tasks {
    val copyAccessWidener by registering(Copy::class) {
        from(project(":common").loom.accessWidenerPath)
        into(file("src/main/resources").absolutePath)
    }

    val copyResources by registering(Copy::class) {
        from(project(":common").file("src/generated")) {
            exclude(".cache/")
        }
        into(file("src/generated"))
    }

    processResources {
        dependsOn(copyAccessWidener)
        dependsOn(copyResources)

        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.property("mod_version")}")
        archiveClassifier.set("dev-slim")
    }

    sourcesJar {
        dependsOn(copyAccessWidener)

        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.property("mod_version")}")
        archiveClassifier.set("sources")
    }

    shadowJar {
        configurations = listOf(shadowCommon)

        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.property("mod_version")}")
        archiveClassifier.set("dev-shadow")

    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })

        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.property("mod_version")}")
    }
}