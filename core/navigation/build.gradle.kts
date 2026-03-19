plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.androidx.navigation3.runtime)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
