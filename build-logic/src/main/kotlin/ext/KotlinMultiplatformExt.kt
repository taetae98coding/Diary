package ext

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun KotlinMultiplatformExtension.sourceSets(
    configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>,
) {
    (this as ExtensionAware).extensions.configure("sourceSets", configure)
}

internal val KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() = (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies
