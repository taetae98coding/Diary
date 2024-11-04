plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.koin.all")
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
    namespace = "${Build.NAMESPACE}.core.account.preferences.memory"
}
