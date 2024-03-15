package plugin.kotlin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

internal class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyAndroid(target.extensions.getByType(CommonExtension::class))
        applyTasks(target)
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.compose")
    }

    private fun applyAndroid(extension: CommonExtension<*, *, *, *, *, *>) = with(extension) {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.10"
        }
    }

    private fun applyTasks(target: Project) = with(target) {
        val rootPath = target.rootProject.file(".").absolutePath

        tasks.withType(KotlinCompile::class.java).all {
            kotlinOptions.freeCompilerArgs = buildList {
                addAll(kotlinOptions.freeCompilerArgs)
                addAll(listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$rootPath/build/compose/metrics"))
                addAll(listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$rootPath/build/compose/reports"))
            }
        }
    }
}