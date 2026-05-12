package plugin.primitive

import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal class ComposePrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        target.configure<ComposeCompilerGradlePluginExtension> {
            metricsDestination.set(target.projectDir.resolve("build/compose/metrics"))
            reportsDestination.set(target.projectDir.resolve("build/compose/reports"))
            stabilityConfigurationFiles.add { target.rootDir.resolve("compose_compiler_config.conf") }
        }
    }
}
