plugins {
    id("diary.multiplatform")
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:memo"))
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.app"
}
