import java.nio.file.Files

/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    alias(libs.plugins.rg.root)
}

val buildMod = project.tasks.register("buildMod") {
    description = "Assemble the jars"

    dependsOn(":x-common:assemble")
    dependsOn(":x-fabric:assemble")
    dependsOn(":x-neoforge:assemble")

    val distDir = file("build/libs")

    doLast {
        logger.info("Preparing $version jars")

        Files.deleteIfExists(distDir.toPath())
        Files.createDirectory(distDir.toPath())

        listOf(
            ":x-common",
            ":x-fabric",
            ":x-neoforge",
        ).forEach { mod ->
            val modProject = project(mod)
            val jars = listOf(
                "${rootProject.name}-${modProject.property("build.sourceset")}-${modProject.version}.jar",
                "${rootProject.name}-${modProject.property("build.sourceset")}-${modProject.version}-sources.jar",
            )

            jars.forEach {
                val dest = file("build/libs/$it")
                dest.createNewFile()

                modProject.file("build/libs/$it").renameTo(dest)
            }
        }
    }
}
