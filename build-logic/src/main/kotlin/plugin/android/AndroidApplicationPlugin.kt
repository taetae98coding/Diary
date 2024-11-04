package plugin.android

import Build
import ext.withAndroidApplication
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidApplicationPlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        target.withPlugin {
            apply("com.android.application")
        }

        androidPlugin.apply(target)

        target.withAndroidApplication {
            defaultConfig {
                targetSdk = Build.ANDROID_TARGET_SDK
            }
        }
    }
}
