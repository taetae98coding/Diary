package plugin.diary

import Build
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.android.AndroidLibPlugin

internal class DiaryMultiplatformPlugin : Plugin<Project> {
    private val androidLibPlugin = AndroidLibPlugin()
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        androidLibPlugin.apply(target)
        applyKotlin(target.extensions.getByType())
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.kotlin.multiplatform")
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension) = with(extension) {
        explicitApi()
        jvmToolchain(Build.JDK_VERSION)

        androidTarget()
        iosArm64()
        iosSimulatorArm64()
        jvm()
        js(IR) {
            browser()
        }

        applyDefaultHierarchyTemplate()
    }
}