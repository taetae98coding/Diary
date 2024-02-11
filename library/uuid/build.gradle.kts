plugins {
    id("diary.module")
}

kotlin {
    sourceSets {
        jsMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
