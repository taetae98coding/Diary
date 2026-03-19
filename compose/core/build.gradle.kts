plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.android.compose.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.library.composeUi)
                implementation(libs.kotlinx.datetime)
                implementation(libs.jetbrains.compose.ui.tooling.preview)
                implementation(libs.jetbrains.compose.material.icons.extended)
                implementation(libs.coil.compose)
                api(libs.jetbrains.compose.material3)

                implementation(libs.multiplatform.markdown.material3)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
