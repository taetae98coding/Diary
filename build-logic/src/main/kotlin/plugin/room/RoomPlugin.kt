package plugin.room

import ext.bundle
import ext.kspAll
import ext.library
import ext.libs
import ext.sourceSets
import ext.withDependency
import ext.withKotlinMultiplatform
import ext.withPlugin
import ext.withRoom
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.android.AndroidLibraryPlugin
import plugin.kotlin.KotlinMultiplatformPlugin

internal class RoomPlugin : Plugin<Project> {
    private val androidLibraryPlugin = AndroidLibraryPlugin()
    private val kotlinMultiplatformPlugin = KotlinMultiplatformPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        androidLibraryPlugin.apply(target)
        kotlinMultiplatformPlugin.apply(target)

        target.withPlugin {
            apply("androidx.room")
            apply("com.google.devtools.ksp")
        }

        target.withKotlinMultiplatform {
            jvm()

            androidTarget()

            iosX64()
            iosArm64()
            iosSimulatorArm64()

//            macosX64()
//            macosArm64()

            applyDefaultHierarchyTemplate()

            sourceSets {
                commonMain {
                    dependencies {
                        implementation(libs.bundle("room"))
                    }
                }
            }
        }

        target.withRoom {
            schemaDirectory("${target.projectDir}/schemas")
        }

        target.withDependency {
            kspAll(libs.library("room-compiler"))
        }
    }
}
