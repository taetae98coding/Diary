plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:google-sign-impl"))
                implementation(compose.runtime)

                api(project(":library:google-sign-api"))
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
    namespace = "${Build.NAMESPACE}.library.google.sign.compose"
}
