plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:auth-impl"))
                implementation(project(":library:firebase-auth-impl"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.auth.koin"
}
