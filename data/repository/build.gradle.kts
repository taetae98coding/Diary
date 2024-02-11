plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:auth-api"))
                implementation(project(":core:coroutines"))

                implementation(project(":data:dto"))
                implementation(project(":data:pref-api"))
                implementation(project(":data:local-api"))
                implementation(project(":data:remote-api"))
                implementation(project(":domain:repository"))

                implementation(project(":library:paging"))
                implementation(project(":library:firebase-firestore-api"))
                implementation(project(":library:kotlin-ext"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.paging.common)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.repository"
}