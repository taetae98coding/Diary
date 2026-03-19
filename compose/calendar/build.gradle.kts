plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.core)
                implementation(projects.library.composeUi)
                implementation(projects.library.kotlinxDatetime)
                api(libs.kotlinx.datetime)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
