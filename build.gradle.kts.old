/*
 * Copyright (c) 2025. gitoido-mc
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 *
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    id("java")
    id("fabric-loom") version "1.11-SNAPSHOT"
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    mavenLocal()
    mavenCentral()
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
    maven("https://maven.architectury.dev/")
    maven("https://maven.wispforest.io/releases/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("https://maven.blamejared.com/")
    maven {
        url = URI("https://packages.aether-mod.net/The-Aether")
        content {
            includeGroup("com.aetherteam.aether")
            includeGroup("com.aetherteam.cumulus")
            includeGroup("com.aetherteam.nitrogen")
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true

    }
}

loom {
    accessWidenerPath = file("src/main/resources/rad-gyms.accesswidener")

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
        getByName("datagen") {
            runDir = "run-datagen"
        }
    }
}

// -XX:+AllowEnhancedClassRedefinition

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("net.benwoodworth.knbt:knbt:0.11.9")
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    // Helpers
    modImplementation("dev.architectury:architectury-fabric:${property("architectury_api_version")}")
    include(
        modImplementation(
            "maven.modrinth:admiral:${property("admiral_version")}+fabric"
        )!!
    )

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${property("aether_version")}-fabric")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}")
    modImplementation("maven.modrinth:rctapi:${property("rctapi_fabric_version")}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }

    jar {
        from("LICENSE")
    }

    compileJava {
        options.release = 21
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.set(listOf("-Xnested-type-aliases"))
        }
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}