package plugin.datastore

import ext.library
import ext.libs
import ext.sourceSets
import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.android.AndroidLibraryPlugin
import plugin.kotlin.KotlinMultiplatformPlugin

internal class DataStorePlugin : Plugin<Project> {
    private val androidLibraryPlugin = AndroidLibraryPlugin()
    private val kotlinMultiplatformPlugin = KotlinMultiplatformPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        androidLibraryPlugin.apply(target)
        kotlinMultiplatformPlugin.apply(target)

        target.withKotlinMultiplatform {
            jvm()

            androidTarget()

            iosX64()
            iosArm64()
            iosSimulatorArm64()

            applyDefaultHierarchyTemplate()

            sourceSets {
                commonMain {
                    dependencies {
                        implementation(libs.library("datastore-preferences"))
                    }
                }
            }
        }
    }
}
