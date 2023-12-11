import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:google-auth-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.kotlinx.coroutines)

                implementation(libs.androidx.core)
                implementation(libs.androidx.credentials)

                // Android 13 and below.
                implementation(libs.androidx.credentials.play.services.auth)

                implementation(libs.google.id)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.google.auth.impl"

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = "\"${getLocalProperty("google.debug.server.client.id")}\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}
