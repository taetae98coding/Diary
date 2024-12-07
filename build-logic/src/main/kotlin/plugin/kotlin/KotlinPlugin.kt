package plugin.kotlin

import Build
import ext.compilerOptions
import ext.withKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.withKotlin {
            jvmToolchain(Build.JDK_VERSION)
            explicitApi()

            compilerOptions {
                freeCompilerArgs.add("-Xwhen-guards")
                freeCompilerArgs.add("-Xnon-local-break-continue")
            }
        }
    }
}
