package plugin.koin

import ext.kspRoom
import ext.library
import ext.libs
import ext.sourceSets
import ext.withDependency
import ext.withKotlinMultiplatform
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal class KoinRoomPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        val libs = target.libs

        target.withPlugin {
            apply("com.google.devtools.ksp")
        }

        target.withKotlinMultiplatform {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(project(":library:koin-room"))

                        implementation(project.dependencies.platform(libs.library("koin-bom")))
                        implementation(libs.library("koin-core"))
                        implementation(project.dependencies.platform(libs.library("koin-annotations-bom")))
                        implementation(libs.library("koin-annotations"))
                    }
                }
            }

            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }

        target.withDependency {
            kspRoom(platform(libs.library("koin-annotations-bom")))
            kspRoom(libs.library("koin-compiler"))
        }
    }
}
