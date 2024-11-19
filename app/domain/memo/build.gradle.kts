plugins {
    id("diary.app.domain")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:account"))
                implementation(project(":app:domain:backup"))
            }
        }
    }
}
