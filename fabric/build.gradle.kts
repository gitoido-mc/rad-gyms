/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
@file:Suppress("MaxLineLength")

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
}

repositories {
    maven("https://maven.fabricmc.net/")
}

architectury {
    platformSetupLoomIde()
    fabric {
        fabricApi {
            configureDataGeneration {
                client = true
                modId = rootProject.property("mod_id") as String
                outputDirectory = project(":common").file("src/generated")
            }
        }
    }
}

val shadowCommon = configurations.create("shadowCommon") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

loom {
    silentMojangMappingsLicense()
    enableTransitiveAccessWideners.set(true)

    runs {
        getByName("client") {
            programArgs(
                "--username=Gitoido",
                "--uuid=23131d78-9edb-48a4-902a-e22e572e9f2b",
            )
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    modImplementation("dev.architectury:architectury-fabric:${property("architectury_api_version")}")
    if (!property("use_cobbled_snapshot").toString().toBooleanStrict()) {
        modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}+${property("minecraft_version")}") {
            isTransitive = false
        }
    } else {
        modImplementation("com.cobblemon:fabric:${property("cobblemon_snapshot_version")}+${property("minecraft_version")}-SNAPSHOT") {
            isTransitive = false
        }
    }

    // Common code
    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(":common", configuration = "transformProductionFabric"))

    //
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_fabric_version")}")
    modImplementation("mod.azure.azurelib:azurelib-common-${rootProject.property("minecraft_version")}:${property("azurelib_version")}")

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${property("aether_version")}-fabric")
}

tasks {
    val copyAccessWidener = register<Copy>("copyAccessWidener") {
        description = "Copy accessWidener from common"
        from(project(":common").file("src/main/resources/rad_gyms.accesswidener"))
        into(file("src/main/resources").absolutePath)
    }

    val cleanupGenerated = register<Delete>("cleanupGenerated") {
        description = "Cleanup generated files"
        dependsOn(copyAccessWidener)
        delete(file("src/generated"))
    }

    val copyGenerated = register<Copy>("copyGenerated") {
        description = "Copy generated files from common"
        dependsOn(cleanupGenerated)
        from(project(":common").file("src/generated")) {
            exclude(".cache/")
        }
        into(file("src/generated"))
    }

    val copyMixin = register<Copy>("copyMixins") {
        description = "Copy mixins from common"
        dependsOn(copyGenerated)
        from(project(":common").file("src/resources/${project.property("mod_id")}.client.mixins.json"))
        from(project(":common").file("src/resources/${project.property("mod_id")}.mixins.json"))
        into(file("src/resources"))
    }

    processResources {
        dependsOn(copyMixin)

        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "version" to project.version,
                "mod_id" to project.property("mod_id"),
                "minecraft_version" to project.property("minecraft_version"),
                "fabric_loader_version" to project.property("fabric_loader_version"),
                "cobblemon_version" to project.property("cobblemon_version"),
                "rctapi_min_version" to project.property("rctapi_min_version"),
            ))
        }
    }

    sourcesJar {
        dependsOn(copyAccessWidener)
    }

    shadowJar {
        configurations = listOf(shadowCommon)

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("shadow")
    }

    remapJar {
        injectAccessWidener = true
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
