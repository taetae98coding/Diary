plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:memo"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.memo"
}
