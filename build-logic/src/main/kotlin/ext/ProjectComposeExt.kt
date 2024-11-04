package ext

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.withCompose(
    action: ComposeExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<ComposeExtension>(),
        block = action,
    )
}

internal fun Project.withComposeCompiler(
    action: ComposeCompilerGradlePluginExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<ComposeCompilerGradlePluginExtension>(),
        block = action,
    )
}
