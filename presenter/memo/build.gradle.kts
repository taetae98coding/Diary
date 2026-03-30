plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.navigation)
                api(projects.core.model)
                api(libs.kotlinx.coroutines.core)
                api(libs.androidx.paging.common)

                implementation(projects.compose.core)
                implementation(projects.library.composeUi)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.androidx.paging.compose)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
