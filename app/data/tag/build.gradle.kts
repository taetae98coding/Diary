plugins {
    id("diary.app.data")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:diary-database"))
                implementation(project(":app:domain:tag"))
            }
        }
    }
}
