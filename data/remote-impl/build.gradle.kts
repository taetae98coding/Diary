plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":data:dto"))
                implementation(project(":data:remote-api"))
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.remote.impl"
}
