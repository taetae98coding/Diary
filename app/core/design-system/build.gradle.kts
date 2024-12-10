import org.jetbrains.compose.resources.ResourcesExtension.ResourceClassGeneration
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

compose {
    resources {
        generateResClass = ResourceClassGeneration.Never
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:color"))
                implementation(project(":library:datetime"))

                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.compose.markdown)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.preview)
            }
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        invokeWhenCreated("androidDebug") {
            dependencies {
                implementation(compose.uiTooling)
            }
        }

        val nonAndroidMain = create("nonAndroidMain")

        nonAndroidMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonAndroidMain)
        iosMain.get().dependsOn(nonAndroidMain)
        macosMain.get().dependsOn(nonAndroidMain)
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.design.system"
}
