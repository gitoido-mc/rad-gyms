/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    common("neoforge", "fabric")
}

val generatedResources = file("src/generated")

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}

loom {
    silentMojangMappingsLicense()
    accessWidenerPath = file("src/main/resources/${rootProject.property("mod_id")}.accesswidener")
}

dependencies {
    // Core deps
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("net.benwoodworth.knbt:knbt:0.11.9")
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    // for Architectury EnvType annotations
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")

    // Cobblemon
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}+${property("minecraft_version")}") {
        isTransitive = false
    }

    // Project deps
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_common_version")}")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    remapJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
