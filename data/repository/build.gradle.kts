plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:auth-api"))

                implementation(project(":data:dto"))
                implementation(project(":data:local-api"))
                implementation(project(":domain:repository"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.paging.common)
            }
        }
    }
}