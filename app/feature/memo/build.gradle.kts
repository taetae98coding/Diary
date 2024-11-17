plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:memo"))
                implementation(project(":app:domain:tag"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.memo"
}
