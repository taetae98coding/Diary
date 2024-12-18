plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:calendar-compose"))

                implementation(project(":app:domain:account"))
                implementation(project(":app:domain:memo"))
                implementation(project(":app:domain:buddy"))
                implementation(project(":app:domain:holiday"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.buddy"
}
