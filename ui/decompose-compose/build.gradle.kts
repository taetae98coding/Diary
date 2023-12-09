plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
                implementation(libs.decompose)
                implementation(libs.decompose.compose)
            }
        }

        iosMain {
            dependencies {
                implementation(compose.animation)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.decompose.compose"
}
