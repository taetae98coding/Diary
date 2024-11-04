package plugin.compose

import ext.withAndroid
import ext.withComposeCompiler
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

internal class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.withPlugin {
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        target.withAndroid {
            buildFeatures {
                compose = true
            }
        }

        target.withComposeCompiler {
            featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
//            featureFlags.add(ComposeFeatureFlag.PausableComposition)

//            stabilityConfigurationFiles.add(RegularFile { target.rootProject.file("compose-stability-configuration-file.txt") })

            metricsDestination.assign(target.rootProject.file("build/compose/metrics"))
            reportsDestination.assign(target.rootProject.file("build/compose/report"))
        }
    }
}
