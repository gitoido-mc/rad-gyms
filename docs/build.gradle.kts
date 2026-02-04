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
version = rootProject.version

tasks.register<NpxTask>("watch") {
    command = "@11ty/eleventy"
    args = listOf("--serve")
    inputs.files(
        "${project.layout.projectDirectory}/package.json",
        "${project.layout.projectDirectory}/package-lock.json"
    )
    inputs.dir("${project.layout.projectDirectory}/src")
    inputs.dir(fileTree("${project.layout.projectDirectory}/node_modules").exclude(".cache"))
}

// Build (for CI)
tasks.register<NpxTask>("build") {
    command = "@11ty/eleventy"
}

tasks.register<Delete>("clean") {
    delete("${project.layout.projectDirectory}/dist")
}