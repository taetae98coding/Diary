plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.androidx.room3.common)
                implementation(libs.androidx.paging.common)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
