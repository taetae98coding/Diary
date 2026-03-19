plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.core)
                implementation(projects.compose.calendar)
                api(projects.presenter.calendar.api)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
