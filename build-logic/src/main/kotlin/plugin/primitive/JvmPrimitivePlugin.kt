package plugin.primitive

import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class JvmPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("diary.primitive.kotlin")
        }

        target.configure<KotlinMultiplatformExtension> {
            jvm()
        }
    }
}
