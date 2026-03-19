package plugin.primitive

import io.github.taetae98coding.diary.gradle.android
import io.github.taetae98coding.diary.gradle.library
import io.github.taetae98coding.diary.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidComposeTestPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.libs

        target.configure<KotlinMultiplatformExtension> {
            android {
                androidResources {
                    enable = true
                }
                withDeviceTest {
                    managedDevices {
                        localDevices {
                            create("sdk33") {
                                device = "Pixel 6"
                                apiLevel = 33
                                systemImageSource = "aosp-atd"
                            }
                            create("sdk35") {
                                device = "Pixel 6"
                                apiLevel = 35
                                systemImageSource = "aosp-atd"
                            }
                        }
                        groups {
                            create("allSdks") {
                                targetDevices.add(localDevices.getByName("sdk33"))
                                targetDevices.add(localDevices.getByName("sdk35"))
                            }
                        }
                    }
                }
            }

            sourceSets {
                getByName("androidDeviceTest") {
                    dependencies {
                        implementation(libs.library("androidx.compose.ui.test.junit4"))
                        implementation(libs.library("androidx.compose.ui.test.manifest"))
                    }
                }
            }
        }
    }
}
