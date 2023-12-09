plugins {
    id("diary.multiplatform")
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.compose"
}
