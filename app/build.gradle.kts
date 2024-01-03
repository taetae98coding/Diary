plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:coroutines"))
                implementation(project(":core:auth-module"))
                implementation(project(":core:firestore-module"))
                implementation(project(":data:pref-impl"))
                implementation(project(":data:local-impl"))
                implementation(project(":data:remote-impl"))
                implementation(project(":data:repository"))
                implementation(project(":domain:usecase"))

                implementation(project(":feature:memo"))
                implementation(project(":feature:more"))
                implementation(project(":feature:account"))
                implementation(project(":feature:calendar"))

                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.app"
}
