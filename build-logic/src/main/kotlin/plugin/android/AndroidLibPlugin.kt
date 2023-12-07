package plugin.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager

internal class AndroidLibPlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        androidPlugin.apply(target)
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("com.android.library")
    }
}