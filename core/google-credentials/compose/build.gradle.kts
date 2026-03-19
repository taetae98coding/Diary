plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.googleCredentials.impl)
                api(projects.core.googleCredentials.api)

                implementation(libs.jetbrains.compose.runtime)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.jetbrains.compose.ui)
            }
        }

        appleMain {
            dependencies {
                implementation(libs.jetbrains.compose.ui)
            }
        }
    }
}
