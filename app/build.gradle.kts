plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:memo"))
                implementation(project(":feature:more"))

                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))
                implementation(project(":ui:decompose-compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.app"
}
