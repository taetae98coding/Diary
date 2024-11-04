package plugin.kotlin

import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinAndroidPlugin : Plugin<Project> {
    private val kotlinPlugin = KotlinPlugin()

    override fun apply(target: Project) {
        target.withPlugin {
            apply("org.jetbrains.kotlin.android")
        }

        kotlinPlugin.apply(target)
    }
}
