plugins {
    id("diary.datastore")
    id("diary.koin.datastore")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:account-preferences"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.account.preferences.datastore"
}
