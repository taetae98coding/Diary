package plugin.primitive

import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KoinPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.libs

        target.plugins {
            apply("io.insert-koin.compiler.plugin")
        }

        target.configure<KotlinMultiplatformExtension> {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(libs.library("koin-core"))
                        implementation(libs.library("koin-annotations"))
                    }
                }
            }
        }
    }
}
