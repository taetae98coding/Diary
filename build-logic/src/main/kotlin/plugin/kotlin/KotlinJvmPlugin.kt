package plugin.kotlin

import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinJvmPlugin : Plugin<Project> {
    private val kotlinPlugin = KotlinPlugin()

    override fun apply(target: Project) {
        target.withPlugin {
            apply("org.jetbrains.kotlin.jvm")
        }

        kotlinPlugin.apply(target)
    }
}
