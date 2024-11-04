plugins {
    id("diary.datastore")
    id("diary.koin.datastore")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:holiday-preferences"))
                implementation(project(":library:datetime"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.holiday.preferences.datastore"
}
