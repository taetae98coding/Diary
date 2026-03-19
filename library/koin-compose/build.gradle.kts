plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.compose)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
