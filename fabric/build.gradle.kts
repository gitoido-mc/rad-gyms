/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
@file:Suppress("MaxLineLength")

plugins {
    alias(libs.plugins.rg.common)
    alias(libs.plugins.shadow)
    alias(libs.plugins.wikiToolkit)
}

architectury {
    platformSetupLoomIde()
    fabric {
        fabricApi {
            configureDataGeneration {
                client = true
                modId = rootProject.property("mod_id") as String
                outputDirectory = project(":common").file("src/generated")
            }
        }
    }
}

wiki {
    wikiAccessToken = providers.systemProperty("moddedmc_gh_token").get()
    docs.create(project.property("mod_id") as String) {
        root = file("../docs/rad_gyms")
    }
}

loom {
    silentMojangMappingsLicense()
    enableTransitiveAccessWideners.set(true)

    runs {
        val wikiExporterParams = mapOf(
            "wiki_exporter.config.path" to "../../docs/rad_gyms/wiki-exporter.config.json",
            "wiki_exporter.enabled" to "true",
        )

        getByName("client") {
            runDirectory.set(file("runClient"))
            programArguments.addAll(
                "--username=Gitoido",
                "--uuid=23131d78-9edb-48a4-902a-e22e572e9f2b",
            )
        }
        getByName("server") {
            runDirectory.set(file("runServer"))
        }
        create("exportClient") {
            client()
            runDirectory.set(file("runClient"))
            systemProperties.putAll(wikiExporterParams)
        }
        create("exportServer") {
            server()
            runDirectory.set(file("runServer"))
            programArguments.add("nogui")
            systemProperties.putAll(wikiExporterParams)
        }
    }
}

val shadowCommon = configurations.create("shadowCommon") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.su5ed.dev/releases") // wiki
}

@Suppress("AvoidDuplicateDependencies")
dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    modCompileOnly(libs.aether.fabric)

    modRuntimeOnly(libs.wikiExporter)

    libs.structurePlacerApi.fabric.let {
        modImplementation(it)
        include(it)
    }
    modImplementation(libs.architectury.fabric)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.rctapi.fabric)
    modImplementation(libs.azurelib.fabric)
    modImplementation(libs.cobblemon.fabric) {
        isTransitive = false
    }

    shadowCommon(project(":common", configuration = "transformProductionFabric"))
    project(":common", configuration = "namedElements").let {
        implementation(it)
        "developmentFabric"(it) { isTransitive = false }
    }
}

tasks {
    val copyAccessWidener = register<Copy>("copyAccessWidener") {
        description = "Copy accessWidener from common"
        from(project(":common").file("src/main/resources/rad_gyms.accesswidener"))
        into(file("src/main/resources").absolutePath)
    }

    val cleanupGenerated = register<Delete>("cleanupGenerated") {
        description = "Cleanup generated files"
        dependsOn(copyAccessWidener)
        delete(file("src/generated"))
    }

    val copyGenerated = register<Copy>("copyGenerated") {
        description = "Copy generated files from common"
        dependsOn(cleanupGenerated)
        from(project(":common").file("src/generated")) {
            exclude(".cache/")
        }
        into(file("src/generated"))
    }

    val copyMixin = register<Copy>("copyMixins") {
        description = "Copy mixins from common"
        dependsOn(copyGenerated)
        from(project(":common").file("src/resources/${project.property("mod_id")}.client.mixins.json"))
        from(project(":common").file("src/resources/${project.property("mod_id")}.mixins.json"))
        into(file("src/resources"))
    }

    processResources {
        dependsOn(copyMixin)

        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to project.version,
                    "mod_id" to project.property("mod_id"),
                    "minecraft_version" to project.property("minecraft_version"),
                    "fabric_loader_version" to project.property("fabric_loader_version"),
                    "cobblemon_version" to project.property("cobblemon_version"),
                    "rctapi_min_version" to project.property("rctapi_min_version"),
                ),
            )
        }
    }

    sourcesJar {
        dependsOn(copyAccessWidener)
    }

    shadowJar {
        configurations = listOf(shadowCommon)

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("shadow")
    }

    remapJar {
        injectAccessWidener = true
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
