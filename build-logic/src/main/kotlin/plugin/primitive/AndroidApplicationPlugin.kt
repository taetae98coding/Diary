package plugin.primitive

import BuildConfig
import com.android.build.api.dsl.ApplicationExtension
import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("com.android.application")
            apply("diary.primitive.kotlin")
        }

        target.configure<ApplicationExtension> {
            compileSdk = BuildConfig.ANDROID_COMPILE_SDK

            defaultConfig {
                minSdk = BuildConfig.ANDROID_MIN_SDK
                targetSdk = BuildConfig.ANDROID_TARGET_SDK
            }
        }
    }
}
