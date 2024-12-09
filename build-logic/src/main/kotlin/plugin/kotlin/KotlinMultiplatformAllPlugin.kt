package plugin.kotlin

import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinMultiplatformAllPlugin : Plugin<Project>{
    private val kotlinMultiplatformPlugin = KotlinMultiplatformPlugin()

    override fun apply(target: Project) {
        kotlinMultiplatformPlugin.apply(target)

        target.withKotlinMultiplatform {
            jvm()

            androidTarget()

            iosX64()
            iosArm64()
            iosSimulatorArm64()

//            macosX64()
//            macosArm64()

            applyDefaultHierarchyTemplate()
        }
    }
}
