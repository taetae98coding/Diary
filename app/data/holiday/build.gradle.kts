plugins {
    id("diary.app.data")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:holiday-preferences"))
                implementation(project(":app:core:holiday-database"))
                implementation(project(":app:core:holiday-service"))
                implementation(project(":app:domain:holiday"))
            }
        }
    }
}
