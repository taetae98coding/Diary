plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:calendar-compose"))
                implementation(project(":app:core:design-system"))
                implementation(project(":app:core:model"))
                
                implementation(project(":library:color"))
                implementation(project(":library:datetime"))

                implementation(compose.material3)
                implementation(libs.compose.material3.adaptive.navigation)
                implementation(libs.lifecycle.compose)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.compose"
}
