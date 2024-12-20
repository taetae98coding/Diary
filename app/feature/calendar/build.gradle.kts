plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:calendar-compose"))

                implementation(project(":app:domain:calendar"))
                implementation(project(":app:domain:holiday"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.calendar"
}
