package plugin.primitive

import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class MultiplatformPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("diary.primitive.ios")
            apply("diary.primitive.jvm")
            apply("diary.primitive.wasm")
        }
        target.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()
        }
    }
}
