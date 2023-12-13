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

                api(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.decompose.compose"
}
