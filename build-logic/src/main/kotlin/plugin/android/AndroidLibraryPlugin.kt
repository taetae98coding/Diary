package plugin.android

import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidLibraryPlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        target.withPlugin {
            apply("com.android.library")
        }

        androidPlugin.apply(target)
    }
}
