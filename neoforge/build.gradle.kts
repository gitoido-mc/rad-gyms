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

val shadowCommon: Configuration = configurations.create("shadowCommon") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

architectury {
    platformSetupLoomIde()
    neoForge()
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

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")
    implementation("thedarkcolour:kotlinforforge-neoforge:${property("kotlin_for_forge_version")}") {
        exclude("net.neoforged.fancymodloader", "loader")
    }

    modCompileOnly("com.aetherteam.aether:aether:${property("aether_version")}-neoforge")

    modImplementation("dev.architectury:architectury-neoforge:${property("architectury_api_version")}")
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_neoforge_version")}")

    with("maven.modrinth:sSdng0L4:${rootProject.property("structure_placer_api_neoforge_version")}") {
        modImplementation(this)
        include(this)
    }

    modImplementation("com.cobblemon:neoforge:${property("cobblemon_version")}+${property("minecraft_version")}") {
        isTransitive = false
    }

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(":common", configuration = "transformProductionFabric"))
}

tasks {
    val cleanupGenerated = register<Delete>("cleanupGenerated") {
        description = "Cleanup generated files"
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

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(
                mapOf(
                    "version" to project.version,
                    "mod_id" to project.property("mod_id"),
                    "minecraft_version" to project.property("minecraft_version"),
                    "neoforge_version" to project.property("neoforge_version"),
                    "cobblemon_version" to project.property("cobblemon_version"),
                    "rctapi_min_version" to project.property("rctapi_min_version"),
                ),
            )
        }
    }

    jar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("shadow")
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        atAccessWideners.add("${project.property("mod_id")}.accesswidener")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
