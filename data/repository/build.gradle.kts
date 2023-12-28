plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
    id("diary.kotest.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:auth-api"))

                implementation(project(":data:dto"))
                implementation(project(":data:pref-api"))
                implementation(project(":data:local-api"))
                implementation(project(":domain:repository"))

                implementation(project(":library:paging"))
                implementation(project(":library:firebase-firestore-api"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.paging.common)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.repository"
}