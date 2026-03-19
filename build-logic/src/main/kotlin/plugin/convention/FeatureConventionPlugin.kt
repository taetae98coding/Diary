package plugin.convention

import io.github.taetae98coding.diary.gradle.androidRuntimeClasspath
import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.libs

        target.plugins {
            apply("diary.primitive.multiplatform.android")
            apply("diary.primitive.compose")
            apply("diary.primitive.koin")
            apply("diary.primitive.android.test")
        }

        target.configure<KotlinMultiplatformExtension> {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(target.project(":compose:core"))
                        implementation(target.project(":core:navigation"))
                        implementation(target.project(":library:koin-compose"))
                        implementation(libs.library("jetbrains-navigation3-ui"))
                        implementation(libs.library("koin-compose"))
                        implementation(libs.library("jetbrains-lifecycle-runtime-compose"))
                        implementation(libs.library("koin-compose-viewmodel"))
                    }
                }
            }
        }

        target.dependencies {
            androidRuntimeClasspath(libs.library("jetbrains-compose-ui-tooling"))
        }
    }
}
