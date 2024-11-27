package plugin.convention

import ext.kspCommon
import ext.library
import ext.libs
import ext.sourceSets
import ext.withDependency
import ext.withKotlinMultiplatform
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.kotest.KotestJvmPlugin
import plugin.kotlin.KotlinMultiplatformCommonPlugin

internal class AppDomainPlugin : Plugin<Project> {
    private val kotlinMultiplatformCommonPlugin = KotlinMultiplatformCommonPlugin()
    private val kotestJvmPlugin = KotestJvmPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        kotlinMultiplatformCommonPlugin.apply(target)

        target.withPlugin {
            apply("com.google.devtools.ksp")
        }

        target.withKotlinMultiplatform {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(project(":library:coroutines"))
                        implementation(project(":library:datetime"))
                        implementation(project(":library:kotlin"))

                        implementation(project.dependencies.platform(libs.library("koin-bom")))
                        implementation(libs.library("koin-core"))
                        implementation(project.dependencies.platform(libs.library("koin-annotations-bom")))
                        implementation(libs.library("koin-annotations"))

                        api(project(":app:core:model"))
                        api(project(":common:exception"))
                    }
                }
            }
        }

        target.withDependency {
            kspCommon(platform(libs.library("koin-annotations-bom")))
            kspCommon(libs.library("koin-compiler"))
        }

        kotestJvmPlugin.apply(target)
    }
}
