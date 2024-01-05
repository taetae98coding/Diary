plugins {
    id("diary.module")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":data:dto"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.paging.common)
            }
        }
    }
}
