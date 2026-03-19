plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.jetbrains.compose.ui)
            }
        }
    }
}
