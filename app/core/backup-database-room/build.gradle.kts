plugins {
    id("diary.room")
    id("diary.koin.room")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:backup-database"))
                implementation(project(":library:coroutines"))
                implementation(project(":library:room"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.backup.database.room"
}
