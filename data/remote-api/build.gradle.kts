plugins {
    id("diary.module")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":data:dto"))
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
