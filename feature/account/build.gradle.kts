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
                implementation(project(":library:google-auth-compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.account"
}
