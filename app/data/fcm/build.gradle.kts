plugins {
    id("diary.app.data")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:diary-service"))
                implementation(project(":app:domain:fcm"))
                implementation(project(":library:firebase-messaging"))
            }
        }
    }
}
