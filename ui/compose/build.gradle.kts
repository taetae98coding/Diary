plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.uiTooling)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.compose"
}
