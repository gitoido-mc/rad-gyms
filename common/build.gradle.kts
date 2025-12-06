/*
 * Copyright (c) 2025. gitoido-mc
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

loom {
    silentMojangMappingsLicense()
    accessWidenerPath = file("src/main/resources/rad-gyms.accesswidener")
}

dependencies {
    // Core deps
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("net.benwoodworth.knbt:knbt:0.11.9")
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

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
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}") { isTransitive = false }

    // Project deps
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_common_version")}")
}