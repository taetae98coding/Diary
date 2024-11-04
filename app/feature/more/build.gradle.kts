plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:account"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.more"
}
