plugins {
    id("diary.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose.instance)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.viewmodel"
}
