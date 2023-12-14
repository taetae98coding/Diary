plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain:repository"))
                implementation(libs.kotlinx.coroutines.core)

                api(project(":domain:entity"))
            }
        }
    }
}
