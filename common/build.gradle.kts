/*
 * Copyright (c) 2025-2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    alias(libs.plugins.rg.common)
}

architectury {
    common("neoforge", "fabric")
}

sourceSets {
    main {
        resources.srcDir(file("src/generated"))
    }
}

loom {
    silentMojangMappingsLicense()
    accessWidenerPath = file("src/main/resources/${rootProject.property("mod_id")}.accesswidener")
}

dependencies {
    // Core deps
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.bundles.common.mixin)
    annotationProcessor(libs.mixin.extras)

    modApi(libs.molang)
    modCompileOnly(libs.azurelib.common)
    modImplementation(libs.rctapi.common)
    modImplementation(libs.cobblemon.common) {
        isTransitive = false
    }
}
