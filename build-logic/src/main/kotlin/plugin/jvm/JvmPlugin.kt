package plugin.jvm

import Build
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal class JvmPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyKotlin(target.extensions.getByType())
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.kotlin.jvm")
    }

    private fun applyKotlin(extension: KotlinJvmProjectExtension) = with(extension) {
        explicitApi()
        jvmToolchain(Build.JDK_VERSION)
    }
}