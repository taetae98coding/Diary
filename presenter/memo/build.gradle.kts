plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.core)
                api(projects.core.model)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.androidx.paging.compose)
                api(libs.kotlinx.coroutines.core)
                api(libs.androidx.paging.common)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
