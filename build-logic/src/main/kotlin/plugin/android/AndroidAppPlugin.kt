package plugin.android

import Build
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class AndroidAppPlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        androidPlugin.apply(target)
        applyKotlin(target.extensions.getByType())
        applyAndroid(target.extensions.getByType())
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("org.jetbrains.kotlin.android")
        apply("com.android.application")
    }

    private fun applyKotlin(extension: KotlinAndroidProjectExtension) = with(extension) {
        explicitApi()
        jvmToolchain(Build.JDK_VERSION)
    }

    private fun applyAndroid(extension: ApplicationExtension) = with(extension) {
        defaultConfig {
            targetSdk = Build.ANDROID_TARGET_SDK
        }
    }
}