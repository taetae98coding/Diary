package plugin.kotest

import ext.bundle
import ext.library
import ext.libs
import ext.sourceSets
import ext.withKotlinMultiplatform
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal class KotestJvmPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        val libs = target.libs

        target.withPlugin {
            apply("io.kotest.multiplatform")
        }

        target.withKotlinMultiplatform {
            sourceSets {
                jvmTest {
                    dependencies {
                        implementation(kotlin("reflect"))
                        implementation(libs.library("kotlinx-coroutines-test"))

                        implementation(libs.bundle("kotest"))
                        implementation(libs.library("kotest-runner-junit5"))
                        implementation(libs.library("mockk"))
                    }
                }
            }
        }

        target.tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}
