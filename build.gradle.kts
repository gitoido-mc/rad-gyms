/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"

    id("dev.architectury.loom") version "1.13-SNAPSHOT" apply false
    id("com.gradleup.shadow") version "9.3.1" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("pl.allegro.tech.build.axion-release") version "1.20.1"
}

architectury {
    minecraft = project.property("minecraft_version") as String
}

repositories {
    mavenCentral()
    maven("https://maven.architectury.dev/")
    maven("https://maven.wispforest.io/releases/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("https://maven.blamejared.com/")
    maven {
        url = URI("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        url = URI("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven {
        url = URI("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroup("software.bernie.geckolib")
        }
    }
    maven {
        url = URI("https://packages.aether-mod.net/The-Aether")
        content {
            includeGroup("com.aetherteam.aether")
            includeGroup("com.aetherteam.cumulus")
            includeGroup("com.aetherteam.nitrogen")
        }
    }
}

val modProjects = listOf(
    "common",
    "fabric",
    "neoforge"
)

modProjects.forEach {
    project(it) {
        apply(plugin = "java")
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        apply(plugin = "pl.allegro.tech.build.axion-release")

        scmVersion {
            tag {
                prefix = "1.7.0+"
                fallbackPrefixes = listOf("1.6.1+")
            }
        }

        group = property("maven_group")!!
        version = scmVersion.version

        repositories {
            mavenCentral()
            maven("https://maven.architectury.dev/")
            maven("https://maven.wispforest.io/releases/")
            maven("https://maven.impactdev.net/repository/development/")
            maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
            maven("https://maven.blamejared.com/")
            maven {
                url = URI("https://www.cursemaven.com")
                content {
                    includeGroup("curse.maven")
                }
            }
            maven {
                url = URI("https://api.modrinth.com/maven")
                content {
                    includeGroup("maven.modrinth")
                }
            }
            maven {
                url = URI("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
                content {
                    includeGroup("software.bernie.geckolib")
                }
            }
            maven {
                url = URI("https://packages.aether-mod.net/The-Aether")
                content {
                    includeGroup("com.aetherteam.aether")
                    includeGroup("com.aetherteam.cumulus")
                    includeGroup("com.aetherteam.nitrogen")
                }
            }
        }

        tasks {
            jar {
                from("LICENSE")
            }

            java {
                withSourcesJar()
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            compileJava {
                options.release = 21
            }

            compileKotlin {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                    freeCompilerArgs.add("-Xreturn-value-checker=check")
                    freeCompilerArgs.add("-Xcontext-parameters")
                }
            }
        }
    }
}

project.tasks.register("buildMod") {
    dependsOn(":common:build")
    dependsOn(":fabric:build")
    dependsOn(":neoforge:build")

    logger.info("Preparing $version jars")

    doLast {
        layout.buildDirectory.file("libs").get().asFile.delete()
        listOf(":common", ":fabric", ":neoforge").forEach { mod ->
            val modProject = project(mod)
            val jars = listOf(
                "${project.name}-${modProject.name}-${modProject.version}.jar",
                "${project.name}-${modProject.name}-${modProject.version}-sources.jar"
            )
            jars.forEach {
                val dest = project.layout.buildDirectory.file("libs/$it").get().asFile
                modProject.layout.buildDirectory.file("libs/$it").get().asFile.renameTo(dest)
            }
        }
    }
}
