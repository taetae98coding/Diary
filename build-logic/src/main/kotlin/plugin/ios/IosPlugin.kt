package plugin.ios

import Build
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class IosPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyKotlin(target.extensions.getByType())
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.kotlin.multiplatform")
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension) = with(extension) {
        explicitApi()
        jvmToolchain(Build.JDK_VERSION)

        iosX64()
        iosArm64()
        iosSimulatorArm64()

        applyDefaultHierarchyTemplate()
    }
}