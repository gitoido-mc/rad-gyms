import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    id("java")
    id("fabric-loom") version "1.9-SNAPSHOT"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("io.github.0ffz.github-packages") version "1.2.1"
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
    maven("https://maven.architectury.dev/")
    maven("https://maven.wispforest.io/releases/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven(githubPackage.invoke("The-Aether-Team/The-Aether"))
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

loom {
    accessWidenerPath = file("src/main/resources/rad-gyms.accesswidener")
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin_version"]}")

    // Helpers
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    modImplementation("dev.architectury:architectury-fabric:${properties["architectury_api_version"]}")
    include(modImplementation("maven.modrinth:admiral:${properties["admiral_version"]}+${properties["minecraft_version"]}+fabric")!!)

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${properties["aether_version"]}-fabric")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${properties["cobblemon_version"]}")

    // OWO
    modImplementation("io.wispforest:owo-lib:${properties["owo_version"]}")
    include("io.wispforest:owo-sentinel:${properties["owo_version"]}")

    // Radical Cobblemon Trainers API
    modImplementation("maven.modrinth:rctapi:${properties["rctapi_fabric_version"]}")
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
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
