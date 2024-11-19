plugins {
    id("diary.app.data")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:account-preferences"))
                implementation(project(":app:domain:account"))
            }
        }
    }
}
