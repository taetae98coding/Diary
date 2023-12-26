plugins {
    id("diary.multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)

                api(libs.decompose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.navigation.core"
}
