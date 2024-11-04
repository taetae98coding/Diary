package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() {
        return extensions.getByType<VersionCatalogsExtension>().named("libs")
    }

internal fun VersionCatalog.library(
    alias: String,
): Provider<MinimalExternalModuleDependency> {
    return findLibrary(alias).get()
}

internal fun VersionCatalog.bundle(
    alias: String,
): Provider<ExternalModuleDependencyBundle> {
    return findBundle(alias).get()
}
