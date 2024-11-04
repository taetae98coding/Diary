plugins {
    id("diary.room")
    id("diary.koin.room")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:holiday-database"))
                implementation(project(":library:room"))
                implementation(project(":library:coroutines"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.holiday.database.room"
}
