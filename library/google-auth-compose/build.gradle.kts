plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:google-auth-impl"))
                implementation(compose.runtime)

                api(project(":library:google-auth-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(compose.ui)
            }
        }

        iosMain {
            dependencies {
                implementation(compose.ui)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.google.auth.compose"
}
