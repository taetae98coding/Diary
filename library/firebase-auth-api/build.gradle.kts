plugins {
    id("diary.multiplatform")
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.auth.api"
}
