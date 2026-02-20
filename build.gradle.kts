/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import pl.allegro.tech.build.axion.release.domain.properties.VersionProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition
import java.net.URI

plugins {
    id("java")
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"

    id("com.gradleup.shadow") version "9.3.1" apply false
    id("dev.architectury.loom") version "1.13-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("pl.allegro.tech.build.axion-release") version "1.20.1"
    id("dev.detekt") version "2.0.0-alpha.2"
    id("org.jetbrains.kotlinx.kover") version "0.9.7"
}

scmVersion {
    releaseBranchNames = listOf("main")
    versionCreator("simple")

    tag {
        prefix = "${project.property("cobblemon_version")}+"
        fallbackPrefixes = listOf("1.6.1+", "1.7.0+", "1.7.1+", "1.7.2+")
    }

    branchVersionCreator.put("bugfix/.*", "simple")
    branchVersionCreator.put(
        "feature/.*",
        VersionProperties.Creator { version: String, position: ScmPosition ->
            "$version-${position.branch.split("/").last()}"
        },
    )

    branchVersionIncrementer.putAll(
        mapOf(
            "main" to "incrementPatch",
            "develop" to "incrementPrerelease",
            "feature/.*" to "incrementPrerelease",
            "hotfix/.*" to "incrementPrerelease",
            "refactor/.*" to "incrementPrerelease",
        ),
    )
}

version = scmVersion.version

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
        url = URI("https://packages.aether-mod.net/The-Aether")
        content {
            includeGroup("com.aetherteam.aether")
            includeGroup("com.aetherteam.cumulus")
            includeGroup("com.aetherteam.nitrogen")
        }
    }
}

val modProjects =
    listOf(
        "common",
        "fabric",
        "neoforge",
    )

dependencies {
    kover(project(":common"))
    kover(project(":fabric"))
    kover(project(":neoforge"))
}

modProjects.forEach {
    project(it) {
        apply(plugin = "java")
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "dev.detekt")

        group = property("maven_group")!!
        version = rootProject.version

        detekt {
            config.setFrom(rootProject.file("detekt.yml"))
            buildUponDefaultConfig = true
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
                url = URI("https://packages.aether-mod.net/The-Aether")
                content {
                    includeGroup("com.aetherteam.aether")
                    includeGroup("com.aetherteam.cumulus")
                    includeGroup("com.aetherteam.nitrogen")
                }
            }
        }

        dependencies {
            detektPlugins("dev.detekt:detekt-rules-ktlint-wrapper:2.0.0-alpha.2")
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

            withType<Detekt>().configureEach {
                exclude("**/build/**")
            }
        }
    }
}

val buildMod by project.tasks.registering {
    dependsOn(":common:build")
    dependsOn(":fabric:build")
    dependsOn(":neoforge:build")
    dependsOn(":docs:build")
    mustRunAfter(
        ":fabric:build",
        ":neoforge:build",
        "build",
    )

    doLast {
        logger.info("Preparing $version jars")
        layout.buildDirectory
            .file("libs")
            .get()
            .asFile
            .delete()
        listOf(":common", ":fabric", ":neoforge").forEach { mod ->
            val modProject = project(mod)
            val jars =
                listOf(
                    "${project.name}-${modProject.name}-${modProject.version}.jar",
                    "${project.name}-${modProject.name}-${modProject.version}-sources.jar",
                )
            jars.forEach {
                val dest =
                    project.layout.buildDirectory
                        .file("libs/$it")
                        .get()
                        .asFile
                dest.ensureParentDirsCreated()
                modProject.layout.buildDirectory
                    .file("libs/$it")
                    .get()
                    .asFile
                    .renameTo(dest)
            }
        }
    }
}
