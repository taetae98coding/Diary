plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core:auth-api"))
                implementation(project(":library:firebase-auth-api"))
            }
        }
    }
}
