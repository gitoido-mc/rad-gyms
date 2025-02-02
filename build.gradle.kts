import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("fabric-loom") version ("1.9-SNAPSHOT")
    kotlin("jvm") version ("2.1.10")
    kotlin("plugin.serialization") version "2.1.10"
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.wispforest.io/releases/")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://www.cursemaven.com")
    maven("https://api.modrinth.com/maven")
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    // Helpers
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}")

    // OWO
    modImplementation("io.wispforest:owo-lib:${property("owo_version")}")
    include("io.wispforest:owo-sentinel:${property("owo_version")}")

    // Radical Cobblemon Trainers API
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_common_version")}")
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${property("rctapi_fabric_version")}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf(
                "version" to project.version
            ))
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