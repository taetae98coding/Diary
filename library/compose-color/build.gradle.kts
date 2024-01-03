plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.compose.color"
}
