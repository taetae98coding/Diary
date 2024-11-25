plugins {
    id("diary.app.data")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:filter-database"))
                implementation(project(":app:domain:calendar"))
            }
        }
    }
}
