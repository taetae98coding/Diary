package plugin.convention

import ext.libs
import ext.sourceSets
import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.koin.KoinCommonPlugin
import plugin.kotlin.KotlinMultiplatformCommonPlugin

internal class AppDataPlugin : Plugin<Project> {
    private val kotlinMultiplatformCommonPlugin = KotlinMultiplatformCommonPlugin()
    private val koinCommonPlugin = KoinCommonPlugin()

    override fun apply(target: Project) {
        kotlinMultiplatformCommonPlugin.apply(target)
        koinCommonPlugin.apply(target)

        target.withKotlinMultiplatform {
            sourceSets {
                commonMain {
                    dependencies {
                        implementation(project(":library:coroutines"))
                        implementation(project(":library:datetime"))
                    }
                }
            }
        }
    }
}
