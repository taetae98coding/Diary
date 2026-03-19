package plugin.primitive

import BuildConfig
import io.github.taetae98coding.diary.gradle.android
import io.github.taetae98coding.diary.gradle.namespace
import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidLibraryPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("com.android.kotlin.multiplatform.library")
        }

        target.configure<KotlinMultiplatformExtension> {
            android {
                namespace = target.namespace()
                compileSdk = BuildConfig.ANDROID_COMPILE_SDK
                minSdk = BuildConfig.ANDROID_MIN_SDK
            }
        }
    }
}
