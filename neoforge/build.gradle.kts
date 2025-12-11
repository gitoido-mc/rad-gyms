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

val shadowCommon: Configuration = configurations.maybeCreate("shadowCommon").apply {
    isCanBeResolved = true
    isCanBeConsumed = false
}

architectury {
    platformSetupLoomIde()
    neoForge()
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
    }
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net")
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:${rootProject.property("neoforge_version")}")
    // Needed for cobblemon
    implementation("thedarkcolour:kotlinforforge-neoforge:${rootProject.property("kotlin_for_forge_version")}") {
        exclude("net.neoforged.fancymodloader", "loader")
    }

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(":common", configuration = "transformProductionNeoForge"))

    // Mod deps
    modImplementation("dev.architectury:architectury-neoforge:${rootProject.property("architectury_api_version")}")
    modImplementation("com.cobblemon:neoforge:${rootProject.property("cobblemon_version")}") { isTransitive = false }
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${rootProject.property("rctapi_neoforge_version")}")
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

        filesMatching("META-INF/neoforge.mods.toml") {
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
        exclude("fabric.mod.json")
        exclude("architectury-common.accessWidener")
        exclude("architectury.common.json")

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