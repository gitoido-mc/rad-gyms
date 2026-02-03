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

    @Suppress("UnstableApiUsage")
    mixin {
        useLegacyMixinAp = true
        defaultRefmapName = "mixins.${rootProject.property("mod_id")}.refmap.json"
    }
}

dependencies {
    // Core deps
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("net.benwoodworth.knbt:knbt:0.11.9")
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    // for Architectury EnvType annotations
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")


    // Mixin additions
    "net.fabricmc:sponge-mixin:0.15.4+mixin.0.8.7".let {
        annotationProcessor(it)
        compileOnly(it)
    }
    "io.github.llamalad7:mixinextras-common:0.5.0".let {
        annotationProcessor(it)
        compileOnly(it)
    }

    // Cobblemon
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}+${property("minecraft_version")}") { isTransitive = false }

    // Project deps
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_common_version")}")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks {
    remapJar {
        archiveBaseName.set("${rootProject.name}")
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("common")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}")
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("sources")
    }
}