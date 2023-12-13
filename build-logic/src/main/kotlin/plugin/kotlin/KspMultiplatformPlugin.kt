package plugin.kotlin

import ext.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KspMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyKotlin(target.extensions.getByType())
        applyTask(target)
    }

    private fun applyPlugin(manager: PluginManager) = with(manager) {
        apply("com.google.devtools.ksp")
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension) = with(extension) {
        sourceSets {
            commonMain {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }
        }
    }

    private fun applyTask(target: Project) = with(target) {
        tasks.withType<KotlinCompile<*>>().all {
            if (name != "kspCommonMainKotlinMetadata") {
                dependsOn("kspCommonMainKotlinMetadata")
            }
        }
    }
}