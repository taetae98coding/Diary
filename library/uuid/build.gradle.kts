plugins {
    id("diary.module")
}

kotlin {
    sourceSets {
        jsMain {
            dependencies {
                implementation("com.benasher44:uuid:0.8.2")
            }
        }
    }
}
