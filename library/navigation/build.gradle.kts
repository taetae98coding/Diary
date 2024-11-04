plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:datetime"))
                api(libs.navigation.common)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.navigation"
}
