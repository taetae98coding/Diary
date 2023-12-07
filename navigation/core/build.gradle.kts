plugins {
    id("diary.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.decompose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.navigation.core"
}
