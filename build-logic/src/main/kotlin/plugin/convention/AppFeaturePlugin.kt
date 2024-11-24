package plugin.convention

import ext.compose
import ext.library
import ext.libs
import ext.sourceSets
import ext.withKotlinMultiplatform
import ext.withKsp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import plugin.android.AndroidLibraryPlugin
import plugin.compose.ComposePlugin
import plugin.koin.KoinAllPlugin
import plugin.kotlin.KotlinMultiplatformAllPlugin

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal class AppFeaturePlugin : Plugin<Project> {
    private val androidLibraryPlugin = AndroidLibraryPlugin()
    private val kotlinMultiplatformAllPlugin = KotlinMultiplatformAllPlugin()
    private val composePlugin = ComposePlugin()
    private val koinAllPlugin = KoinAllPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        androidLibraryPlugin.apply(target)
        kotlinMultiplatformAllPlugin.apply(target)
        composePlugin.apply(target)
        koinAllPlugin.apply(target)

        target.withKotlinMultiplatform {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(project(":app:core:compose"))
                        implementation(project(":app:core:design-system"))
                        implementation(project(":app:core:navigation"))

                        implementation(project(":library:color"))
                        implementation(project(":library:kotlin"))
                        implementation(project(":library:navigation"))
                        implementation(project(":library:coroutines"))
                        implementation(project(":library:datetime"))
                        implementation(project(":library:shimmer-m3"))

                        implementation(compose.material3)
                        implementation(libs.library("compose-material3-adaptive-navigation"))

                        implementation(libs.library("navigation-compose"))

                        implementation(project.dependencies.platform(libs.library("koin-bom")))
                        implementation(libs.library("koin-compose-viewmodel-navigation"))
                    }
                }

                androidMain {
                    dependencies {
                        implementation(compose.preview)
                    }
                }

                invokeWhenCreated("androidDebug") {
                    dependencies {
                        implementation(compose.uiTooling)
                    }
                }
            }
        }

        target.withKsp {
            arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
            arg("KOIN_DEFAULT_MODULE", "false")
        }
    }
}
