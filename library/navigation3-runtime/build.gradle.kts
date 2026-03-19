import io.github.taetae98coding.diary.gradle.nonAndroidMain

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.jetbrains.compose.runtime)
                api(libs.androidx.navigation3.runtime)
            }
        }

        nonAndroidMain {
            dependencies {
                implementation(libs.androidx.compose.runtime.retain)
            }
        }
    }
}
