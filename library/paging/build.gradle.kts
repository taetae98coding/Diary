plugins {
    id("diary.module")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.paging.common)
            }
        }
    }
}
