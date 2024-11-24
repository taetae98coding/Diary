package plugin.kotlin

import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinMultiplatformCommonPlugin : Plugin<Project>{
    private val kotlinMultiplatformPlugin = KotlinMultiplatformPlugin()

    override fun apply(target: Project) {
        kotlinMultiplatformPlugin.apply(target)

        target.withKotlinMultiplatform {
            jvm()

            iosX64()
            iosArm64()
            iosSimulatorArm64()

            applyDefaultHierarchyTemplate()
        }
    }
}
