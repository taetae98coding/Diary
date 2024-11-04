package plugin.android

import Build
import ext.withAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        target.withAndroid {
            defaultConfig {
                compileSdk = Build.ANDROID_COMPILE_SDK
                minSdk = Build.ANDROID_MIN_SDK
            }
        }
    }
}
