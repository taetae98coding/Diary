plugins {
    id("diary.multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.immutable)

                api(libs.decompose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.navigation.core"
}
