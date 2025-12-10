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

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    enableTransitiveAccessWideners.set(true)
//    accessWidenerPath = projectDir.resolve("src/main/resources/rad-gyms.accesswidener")
    silentMojangMappingsLicense()

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "mixins.${project.name}.refmap.json"
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
    }
}

fabricApi {
    configureDataGeneration {
        client = true
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
}

tasks {
    val copyAccessWidener by registering(Copy::class) {
        from(project(":common").loom.accessWidenerPath)
        into(file("src/main/resources").absolutePath)
    }

    processResources {
        dependsOn(copyAccessWidener)

        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }
    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-shadow")

        configurations = listOf(shadowCommon)
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }

    sourcesJar {
        dependsOn(copyAccessWidener)
    }
}