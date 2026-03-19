package plugin.primitive

import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class JvmTestPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.libs

        target.configure<KotlinMultiplatformExtension> {
            sourceSets {
                jvmTest {
                    dependencies {
                        implementation(libs.library("kotlinx-coroutines-test"))
                        implementation(libs.library("kotest-framework-engine"))
                        implementation(libs.library("kotest-assertions-core"))
                        implementation(libs.library("mockk"))
                        implementation(libs.library("fixture-monkey"))
                        implementation(libs.library("turbine"))
                        runtimeOnly(libs.library("kotest-runner-junit5"))
                    }
                }
            }
        }

        target.tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}
