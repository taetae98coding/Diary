package plugin.kotlin

import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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

            applyDefaultHierarchyTemplate()
        }
    }
}
