plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:data:memo"))
                implementation(project(":app:data:account"))
                implementation(project(":app:data:credential"))
                implementation(project(":app:data:holiday"))
                implementation(project(":app:data:backup"))
                implementation(project(":app:data:fetch"))
                implementation(project(":app:data:fcm"))

                implementation(project(":app:domain:memo"))
                implementation(project(":app:domain:account"))
                implementation(project(":app:domain:credential"))
                implementation(project(":app:domain:holiday"))
                implementation(project(":app:domain:backup"))
                implementation(project(":app:domain:fetch"))
                implementation(project(":app:domain:fcm"))

                implementation(project(":app:core:coroutines"))
                implementation(project(":app:core:diary-service"))
                implementation(project(":app:core:holiday-service"))

                implementation(project(":app:feature:memo"))
                implementation(project(":app:feature:calendar"))
                implementation(project(":app:feature:more"))
                implementation(project(":app:feature:account"))

                implementation(project(":library:datetime"))
                implementation(project(":library:firebase-messaging"))

                implementation(compose.material3AdaptiveNavigationSuite)
                implementation(libs.compose.material3.adaptive)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.app"
}
