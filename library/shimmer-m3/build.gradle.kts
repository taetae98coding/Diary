plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:color"))
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.shimmer.m3"
}
