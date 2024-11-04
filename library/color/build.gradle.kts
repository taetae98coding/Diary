plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.color"
}
