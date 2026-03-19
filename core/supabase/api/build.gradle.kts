plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.serialization.json)
                api(libs.ktor.client.core)
            }
        }
    }
}
