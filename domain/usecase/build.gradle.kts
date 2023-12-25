plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
    id("diary.kotest.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain:exception"))
                implementation(project(":domain:repository"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.paging.common)

                api(project(":domain:entity"))
            }
        }
    }
}
