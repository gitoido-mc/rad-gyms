/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

group = rootProject.property("maven_group")!!
version = rootProject.property("mod_version")!!

tasks {
    // Convenience tasks to have it all in one gradle buildscript of root project

    // Watch (local development with livereload)
    maybeCreate("watch", NpxTask::class).apply {
        command = "@11ty/eleventy"
        args = listOf("--serve")
        inputs.files(listOf("package.json", "package-lock.json"))
        inputs.dir("src")
        inputs.dir(fileTree("node_modules").exclude(".cache"))
    }

    // Build (for CI)
    maybeCreate("build", NpxTask::class).apply {
        command = "@11ty/eleventy"
        inputs.files(listOf("package.json", "package-lock.json"))
        inputs.dir("src")
        inputs.dir(fileTree("node_modules").exclude(".cache"))
        outputs.dir("dist")
    }
}