plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:firebase-firestore-impl"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.firestore.module"
}
