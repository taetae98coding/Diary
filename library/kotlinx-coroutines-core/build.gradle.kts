plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.core)
            }
        }
    }
}
