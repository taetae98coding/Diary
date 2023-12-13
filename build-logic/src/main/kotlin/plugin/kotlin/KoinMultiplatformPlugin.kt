package plugin.kotlin

import ext.kspCommonMain
import ext.libs
import ext.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KoinMultiplatformPlugin : Plugin<Project> {
    private val kspMultiplatformPlugin = KspMultiplatformPlugin()

    override fun apply(target: Project) {
        kspMultiplatformPlugin.apply(target)
        applyKotlin(target.extensions.getByType(), target.libs)
        applyDependency(target.dependencies, target.libs)
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension, libs: VersionCatalog) = with(extension) {
        sourceSets {
            commonMain {
                dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(project.dependencies.platform(libs.findLibrary("koin-annotations-bom").get()))
                    implementation(libs.findLibrary("koin-annotations").get())
                }
            }
        }
    }

    private fun applyDependency(handler: DependencyHandler, libs: VersionCatalog) = with(handler) {
        kspCommonMain(platform(libs.findLibrary("koin-annotations-bom").get()))
        kspCommonMain(libs.findLibrary("koin-compiler").get())
    }
}