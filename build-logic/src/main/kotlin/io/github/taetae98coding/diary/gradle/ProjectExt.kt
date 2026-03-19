package io.github.taetae98coding.diary.gradle

import BuildConfig
import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.plugins(block: PluginContainer.() -> Unit) {
    with(plugins, block)
}

public fun Project.namespace(): String {
    val suffix = path.removePrefix(":").replace(":", ".")
    return if (suffix.startsWith("library")) {
        "${BuildConfig.NAMESPACE.removeSuffix(".diary")}.$suffix"
    } else {
        "${BuildConfig.NAMESPACE}.$suffix"
    }
}

public fun Project.getLocalProperties(): Properties {
    val file = project.rootProject.file("local.properties")

    return Properties().apply {
        file
            .inputStream()
            .buffered()
            .use { load(it) }
    }
}
