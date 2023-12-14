plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:auth-api"))
                implementation(project(":domain:repository"))
            }
        }
    }
}