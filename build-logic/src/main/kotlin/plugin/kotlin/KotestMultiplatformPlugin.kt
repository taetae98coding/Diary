package plugin.kotlin

import ext.libs
import ext.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KotestMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyKotlin(target.extensions.getByType(), target.libs)
        applyTask(target)
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("io.kotest.multiplatform")
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension, libs: VersionCatalog) = with(extension) {
        sourceSets {
            commonTest {
                dependencies {
                    implementation(libs.findBundle("kotest").get())
                }
            }

            jvmTest {
                dependencies {
                    implementation(libs.findLibrary("mockk").get())
                    runtimeOnly(libs.findLibrary("kotest-junit5").get())
                }
            }
        }
    }

    private fun applyTask(target: Project) = with(target) {
        tasks.named<Test>("jvmTest") {
            useJUnitPlatform()
        }
    }
}
