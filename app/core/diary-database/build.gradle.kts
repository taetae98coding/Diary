plugins {
    id("diary.kotlin.multiplatform.common")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":app:core:model"))
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
