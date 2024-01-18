plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))
                implementation(project(":ui:entity"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.tag"
}
