package plugin.primitive

import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class ComposePrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        target.extensions
            .findByType<KotlinMultiplatformExtension>()
            ?.apply {
                compilerOptions {
                    optIn.addAll(
                        "androidx.compose.ui.ExperimentalComposeUiApi",
                        "androidx.compose.foundation.ExperimentalFoundationApi",
                        "androidx.compose.material3.ExperimentalMaterial3Api",
                        "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
                        "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
                    )
                }
            }

        target.configure<ComposeCompilerGradlePluginExtension> {
            metricsDestination.set(target.projectDir.resolve("build/compose/metrics"))
            reportsDestination.set(target.projectDir.resolve("build/compose/reports"))
            stabilityConfigurationFiles.add { target.rootDir.resolve("compose_compiler_config.conf") }
        }
    }
}
