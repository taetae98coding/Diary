package plugin.convention

import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class DomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("diary.primitive.multiplatform")
            apply("diary.primitive.koin")
            apply("diary.primitive.jvm.test")
        }

        val libs = target.libs

        target.configure<KotlinMultiplatformExtension> {
            sourceSets {
                commonMain {
                    dependencies {
                        api(project(":core:model"))
                        api(project(":library:kotlinx-coroutines-core"))
                        api(project(":library:paging-common"))
                    }
                }
            }
        }
    }
}
