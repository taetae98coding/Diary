plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.koin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.datastore.api)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.androidx.datastore.core)
            }
        }
    }
}
