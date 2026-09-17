import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories


repositories {
    maven("https://maven.cobbled-creators.org/releases") // cobbled molang
    maven("https://artefacts.cobblemon.com/releases") // cobblemon
    maven("https://maven.azuredoom.com/mods")
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
