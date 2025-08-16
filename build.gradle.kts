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
    id("fabric-loom") version "1.9-SNAPSHOT"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
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
    maven {
        url = URI("https://maven.terraformersmc.com/")
        name = "TerraformersMC" // EMI
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
    val clientConfig = runConfigs.getByName("client")
    clientConfig.programArg("--username=AshKetchum")
    clientConfig.programArg("--uuid=93e4e551-589a-41cb-ab2d-435266c8e035")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin_version"]}")

    // Helpers
    modImplementation("dev.architectury:architectury-fabric:${properties["architectury_api_version"]}")
    include(
        modImplementation(
            "maven.modrinth:admiral:${properties["admiral_version"]}+${properties["minecraft_version"]}+fabric"
        )!!
    )

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${properties["aether_version"]}-fabric")

    // Cobblemon

    if (properties["uses_snapshots"].toString().toBooleanStrict()) {
        modImplementation("com.cobblemon:fabric:${properties["cobblemon_version_snapshot"]}")
    } else {
        modImplementation("com.cobblemon:fabric:${properties["cobblemon_version"]}")
    }
    // Radical Cobblemon Trainers API
    if (properties["uses_snapshots"].toString().toBooleanStrict()) {
        modImplementation("com.gitlab.srcmc:rctapi-fabric-1.21.1:${properties["rctapi_fabric_version_snapshot"]}")
    } else {
        modImplementation("maven.modrinth:rctapi:${properties["rctapi_fabric_version"]}")
    }

    // Recipes
    modCompileOnlyApi("mezz.jei:jei-${properties["minecraft_version"]}-fabric-api:${properties["jei_version"]}")
    if (project.hasProperty("enable_jei") && properties["enable_jei"] == true) {
        modRuntimeOnly("mezz.jei:jei-${properties["minecraft_version"]}-fabric:${properties["jei_version"]}")
    }

    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${properties["rei_version"]}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${properties["rei_version"]}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${properties["cloth_config_version"]}")
    modApi("dev.architectury:architectury-fabric:${properties["architectury_api_version"]}")
    if (project.hasProperty("enable_rei")) {
        modCompileOnly("me.shedaniel:RoughlyEnoughItems-fabric:${properties["rei_version"]}")
        if (properties["enable_rei"] == true) {
            modRuntimeOnly("me.shedaniel:RoughlyEnoughItems-fabric:${properties["rei_version"]}")
        }
    }

    modCompileOnly("dev.emi:emi-fabric:${properties["emi_version"]}+${properties["minecraft_version"]}")
    if (project.hasProperty("enable_emi") && properties["enable_emi"] == true) {
        modLocalRuntime("dev.emi:emi-fabric:${properties["emi_version"]}+${properties["minecraft_version"]}")
    }
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

    runClient {
        args("--username", "Developer")
    }

    jar {
        from("LICENSE")
    }

    compileJava {
        options.release = 21
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
