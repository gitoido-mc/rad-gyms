/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
        freeCompilerArgs.addAll(
            "-Xnullability-annotations=@org.jspecify.annotations:warn",
            "-Xreturn-value-checker=check",
        )
    }
}

spotless {
    kotlin {
        // version, editorConfigPath, editorConfigOverride and customRuleSets are all optional
        ktlint("1.8.0")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:no-wildcard-imports"
        }
    }
}

val version = providers.gradleProperty("version")
val modId = providers.gradleProperty("mod_id")
val cobblemonVersion = providers.gradleProperty("cobblemon_version")
val rctApiVersion = providers.gradleProperty("rctapi_min_version")

tasks {
    jar {
        from("LICENSE")
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    compileJava {
        options.release =
            libs.versions.java
                .get()
                .toInt()
    }
}
