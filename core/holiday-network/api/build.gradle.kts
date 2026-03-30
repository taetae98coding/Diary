plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.core)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
