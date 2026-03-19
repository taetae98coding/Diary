plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.androidx.room3.common)
            }
        }
    }
}
