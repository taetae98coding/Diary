package plugin.primitive

import io.github.taetae98coding.diary.gradle.android
import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidTestPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.libs

        target.configure<KotlinMultiplatformExtension> {
            android {
                withHostTest {
                    isIncludeAndroidResources = true
                }
            }

            sourceSets {
                getByName("androidHostTest") {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(libs.library("androidx-test-core"))
                        implementation(libs.library("kotlinx-coroutines-test"))
                        implementation(libs.library("robolectric"))
                        implementation(libs.library("kotest-assertions-core"))
                        implementation(libs.library("mockk"))
                        implementation(libs.library("fixture-monkey"))
                        implementation(libs.library("turbine"))
                    }
                }
            }
        }
    }
}
