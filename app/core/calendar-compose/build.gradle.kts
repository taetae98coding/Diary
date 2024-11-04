import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:resources"))
                implementation(project(":app:core:design-system"))
                implementation(project(":library:color"))
                implementation(project(":library:datetime"))

                implementation(compose.material3)
                implementation(libs.lifecycle.compose)
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
    }
}

compose {
    resources {
        generateResClass = ResourcesExtension.ResourceClassGeneration.Never
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.calendar.compose"
}
