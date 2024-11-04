plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.koin.all")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:diary-database"))
                implementation(project(":library:coroutines"))
                implementation(project(":library:datetime"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.diary.database.memory"
}
