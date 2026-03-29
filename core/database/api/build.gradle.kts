plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.room3.common)
                implementation(libs.androidx.paging.common)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
