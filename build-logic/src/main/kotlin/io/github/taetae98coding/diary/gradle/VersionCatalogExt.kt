package io.github.taetae98coding.diary.gradle

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

internal fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(alias).get()
}
