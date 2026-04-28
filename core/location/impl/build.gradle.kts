plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.koin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                api(projects.core.location.api)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.google.play.services.location)
                implementation(libs.kotlinx.coroutines.play.services)
            }
        }
    }
}
