/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    alias(libs.plugins.rg.root)
}

// architectury {
//    minecraft = project.property("minecraft_version") as String
// }
//
// repositories {
//    mavenCentral()
//    maven("https://maven.architectury.dev/")
//    maven("https://maven.internal.gito.lol/")
//    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
//    maven("https://maven.blamejared.com/")
//    maven {
//        url = uri("https://www.cursemaven.com")
//        content {
//            includeGroup("curse.maven")
//        }
//    }
//    maven {
//        url = uri("https://api.modrinth.com/maven")
//        content {
//            includeGroup("maven.modrinth")
//        }
//    }
//    maven {
//        url = uri("https://packages.aether-mod.net/The-Aether")
//        content {
//            includeGroup("com.aetherteam.aether")
//            includeGroup("com.aetherteam.cumulus")
//            includeGroup("com.aetherteam.nitrogen")
//        }
//    }
// }
//
// val modProjects = listOf(
//    "common",
//    "fabric",
//    "neoforge",
// )
//
// modProjects.forEach {
//    project(it) {
//        apply(plugin = "java")
//        apply(plugin = "org.jetbrains.kotlin.jvm")
//        apply(plugin = "com.diffplug.spotless")
//
//        group = property("maven_group")!!
//        version = rootProject.version
//
//        repositories {
//            mavenCentral()
//            maven(url = "${rootProject.projectDir}/deps")
//            maven("https://maven.architectury.dev/")
//            maven("https://maven.wispforest.io/releases/")
//            maven("https://maven.impactdev.net/repository/development/")
//            maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
//            maven("https://maven.blamejared.com/")
//            maven("https://maven.azuredoom.com/mods")
//            maven {
//                url = uri("https://www.cursemaven.com")
//                content {
//                    includeGroup("curse.maven")
//                }
//            }
//            maven {
//                url = uri("https://api.modrinth.com/maven")
//                content {
//                    includeGroup("maven.modrinth")
//                }
//            }
//            maven {
//                url = uri("https://packages.aether-mod.net/The-Aether")
//                content {
//                    includeGroup("com.aetherteam.aether")
//                    includeGroup("com.aetherteam.cumulus")
//                    includeGroup("com.aetherteam.nitrogen")
//                }
//            }
//        }
//
//        spotless {
//            kotlin {
//                // version, editorConfigPath, editorConfigOverride and customRuleSets are all optional
//                ktlint("1.8.0")
//                suppressLintsFor {
//                    step = "ktlint"
//                    shortCode = "standard:no-wildcard-imports"
//                }
//            }
//        }
//

// }
//
// val buildMod = project.tasks.register("buildMod") {
//    description = "Assemble the jars"
//
//    dependsOn(":common:build")
//    dependsOn(":fabric:build")
//    dependsOn(":neoforge:build")
//    mustRunAfter(
//        ":fabric:build",
//        ":neoforge:build",
//        "build",
//    )
//
//    doLast {
//        logger.info("Preparing $version jars")
//
//        layout.buildDirectory.file("libs").get().asFile.delete()
//
//        listOf(":common", ":fabric", ":neoforge").forEach { mod ->
//            val modProject = project(mod)
//            val jars = listOf(
//                "${project.name}-${modProject.name}-${modProject.version}.jar",
//                "${project.name}-${modProject.name}-${modProject.version}-sources.jar",
//            )
//
//            jars.forEach {
//                val dest = project.layout.buildDirectory.file("libs/$it").get().asFile
//                dest.ensureParentDirsCreated()
//
//                modProject.layout.buildDirectory.file("libs/$it").get().asFile.renameTo(dest)
//            }
//        }
//    }
// }
