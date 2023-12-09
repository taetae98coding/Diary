package plugin.compose

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyAndroid(target.extensions.getByType(CommonExtension::class))
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.compose")
    }

    private fun applyAndroid(extension: CommonExtension<*, *, *, *, *>) = with(extension) {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.6"
        }
    }
}