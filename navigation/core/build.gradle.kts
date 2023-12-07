plugins {
    id("diary.multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.decompose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.navigation.core"
}
