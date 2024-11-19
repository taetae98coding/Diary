plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:account"))
                implementation(project(":app:domain:credential"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.more"
}
