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
                api(projects.presenter.memo.api)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
