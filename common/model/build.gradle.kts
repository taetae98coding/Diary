plugins {
    id("diary.kotlin.multiplatform.common")
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
