/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import pl.allegro.tech.build.axion.release.domain.properties.VersionProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    base
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
    alias(libs.plugins.axionRelease)
}

scmVersion {
    releaseBranchNames = listOf("main")
    versionCreator("simple")

    tag {
        prefix = "${project.property("cobblemon_version")}+"
        fallbackPrefixes = listOf("1.6.1+", "1.7.0+", "1.7.1+", "1.7.2+")
    }

    branchVersionCreator.put("hotfix/.*", "simple")
    branchVersionCreator.put(
        "release/.*",
        VersionProperties.Creator { _: String, position: ScmPosition ->
            position.branch.split("/").last()
        },
    )
    branchVersionCreator.put(
        "feature/.*",
        VersionProperties.Creator { version: String, position: ScmPosition ->
            "$version-${position.branch.split("/").last()}"
        },
    )

    branchVersionIncrementer.putAll(
        mapOf(
            "main" to "incrementPatch",
            "release/.*" to "incrementPrerelease",
            "develop" to "incrementPrerelease",
            "feature/.*" to "incrementPrerelease",
            "hotfix/.*" to "incrementPrerelease",
            "refactor/.*" to "incrementPrerelease",
        ),
    )
}

version = scmVersion.version

spotless {
    kotlin {
        ktlint("1.8.0")
        suppressLintsFor {
            step = "ktlint"
            shortCode = "standard:no-wildcard-imports"
        }
    }
}

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
        options.release = libs.versions.java.get().toInt()
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
            freeCompilerArgs.add("-Xreturn-value-checker=check")
        }
    }
}
