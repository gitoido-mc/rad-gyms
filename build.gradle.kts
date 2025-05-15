import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI


plugins {
    id("java")
    id("fabric-loom") version "1.9-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.5"
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
    maven("https://modmaven.dev/")
    maven(githubPackage.invoke("The-Aether-Team/The-Aether"))
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
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")
    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin_version"]}")

    // Helpers
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    include(
        modImplementation(
            "maven.modrinth:admiral:${properties["admiral_version"]}+${properties["minecraft_version"]}+fabric"
        )!!
    )
    modImplementation("dev.architectury:architectury-fabric:${properties["architectury_api_version"]}")
    modImplementation("io.wispforest:owo-lib:${properties["owo_version"]}")
    include("io.wispforest:owo-sentinel:${properties["owo_version"]}")

    // Integrations
    modImplementation(
        "software.bernie.geckolib:geckolib-fabric-${properties["minecraft_version"]}:${properties["geckolib_version"]}"
    )

    // Compat
    modCompileOnly("com.aetherteam.aether:aether:${properties["aether_version"]}-fabric")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${properties["cobblemon_version"]}")

    // Radical Cobblemon Trainers API
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${properties["rctapi_common_version"]}")
    modImplementation("curse.maven:radical-cobblemon-trainers-api-1152792:${properties["rctapi_fabric_version"]}")

    // Recipes
    modCompileOnlyApi("mezz.jei:jei-${properties["minecraft_version"]}-fabric-api:${properties["jei_version"]}")
    if (project.hasProperty("enable_jei") && properties["enable_jei"] == "true") {
        modRuntimeOnly("mezz.jei:jei-${properties["minecraft_version"]}-fabric:${properties["jei_version"]}")
    }

    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${properties["rei_version"]}")
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${properties["rei_version"]}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${properties["cloth_config_version"]}")
    modApi("dev.architectury:architectury-fabric:${properties["architectury_api_version"]}")
    if (project.hasProperty("enable_rei") && properties["enable_rei"] == "true") {
        modCompileOnly("me.shedaniel:RoughlyEnoughItems-fabric:${properties["rei_version"]}")
    }

    modCompileOnly("dev.emi:emi-fabric:${properties["emi_version"]}+${properties["minecraft_version"]}")
    if (project.hasProperty("enable_emi") && properties["enable_emi"] == "true") {
        modLocalRuntime("dev.emi:emi-fabric:${properties["emi_version"]}+${properties["minecraft_version"]}")
    }
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        exclude("META-INF")
    }

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

    remapJar {
        // wait until the shadowJar is done
        dependsOn(shadowJar)
        mustRunAfter(shadowJar)
        // Set the input jar for the task. Here use the shadow Jar that include the .class of the transitive dependency
        inputFile = file(shadowJar.get().archiveFile.get().asFile)
    }
}
