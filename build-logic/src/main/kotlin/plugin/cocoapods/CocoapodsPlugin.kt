package plugin.cocoapods

import ext.cocoapods
import ext.withKotlinMultiplatform
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

internal class CocoapodsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.withPlugin {
            apply("org.jetbrains.kotlin.native.cocoapods")
        }

        target.withKotlinMultiplatform {
            cocoapods {
                xcodeConfigurationToNativeBuildType["DevDebug"] = NativeBuildType.DEBUG
                xcodeConfigurationToNativeBuildType["DevRelease"] = NativeBuildType.RELEASE
                xcodeConfigurationToNativeBuildType["RealDebug"] = NativeBuildType.DEBUG
                xcodeConfigurationToNativeBuildType["RealRelease"] = NativeBuildType.RELEASE
            }
        }
    }
}
