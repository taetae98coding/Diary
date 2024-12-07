plugins {
    id("diary.app.feature")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:domain:account"))
                implementation(project(":app:domain:buddy"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.buddy"
}
