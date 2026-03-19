package plugin.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal class KotlinPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<KotlinProjectExtension> {
            explicitApi()
        }

        target.extensions
            .findByType<KotlinMultiplatformExtension>()
            ?.apply {
                compilerOptions {
                    optIn.addAll(
                        "kotlin.uuid.ExperimentalUuidApi",
                    )
                    freeCompilerArgs.addAll(
                        "-Xexpect-actual-classes",
                    )
                }
            }
    }
}
