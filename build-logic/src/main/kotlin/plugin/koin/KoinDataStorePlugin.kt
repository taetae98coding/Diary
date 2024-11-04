package plugin.koin

import ext.kspDataStore
import ext.library
import ext.libs
import ext.sourceSets
import ext.withDependency
import ext.withKotlinMultiplatform
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoinDataStorePlugin : Plugin<Project>{
    override fun apply(target: Project) {
        val libs = target.libs

        target.withPlugin {
            apply("com.google.devtools.ksp")
        }

        target.withKotlinMultiplatform {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(project(":library:koin-datastore"))

                        implementation(project.dependencies.platform(libs.library("koin-bom")))
                        implementation(libs.library("koin-core"))
                        implementation(project.dependencies.platform(libs.library("koin-annotations-bom")))
                        implementation(libs.library("koin-annotations"))
                    }
                }
            }
        }

        target.withDependency {
            kspDataStore(platform(libs.library("koin-annotations-bom")))
            kspDataStore(libs.library("koin-compiler"))
        }
    }
}
