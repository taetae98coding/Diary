package plugin.kotlin

import Build
import ext.withKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.withKotlin {
            jvmToolchain(Build.JDK_VERSION)
            explicitApi()
        }
    }
}
