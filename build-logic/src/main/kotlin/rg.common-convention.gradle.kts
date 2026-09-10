/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    base
    id("java")
    alias(libs.plugins.architectury.dev)
    alias(libs.plugins.architectury.plugin)
    alias(libs.plugins.rg.shared)
}

repositories {
    maven("https://artefacts.cobblemon.com/releases") // cobblemon
    maven {
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }

    // Aether compat
    maven("https://maven.wispforest.io/releases/")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven {
        url = uri("https://packages.aether-mod.net/The-Aether")
        content {
            includeGroup("com.aetherteam.aether")
            includeGroup("com.aetherteam.cumulus")
            includeGroup("com.aetherteam.nitrogen")
        }
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    remapJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("sources")
    }
}
