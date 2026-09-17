import org.gradle.kotlin.dsl.base
import pl.allegro.tech.build.axion.release.domain.properties.VersionProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    base
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
