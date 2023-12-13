plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))
                implementation(project(":ui:decompose-compose"))

                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.memo"
}
