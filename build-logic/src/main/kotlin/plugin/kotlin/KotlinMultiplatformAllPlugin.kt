package plugin.kotlin

import ext.withKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

@OptIn(ExperimentalWasmDsl::class)
internal class KotlinMultiplatformAllPlugin : Plugin<Project>{
    private val kotlinMultiplatformPlugin = KotlinMultiplatformPlugin()

    override fun apply(target: Project) {
        kotlinMultiplatformPlugin.apply(target)

        target.withKotlinMultiplatform {
            jvm()

            wasmJs {
                browser()
            }

            androidTarget()

            iosX64()
            iosArm64()
            iosSimulatorArm64()

            applyDefaultHierarchyTemplate()
        }
    }
}
