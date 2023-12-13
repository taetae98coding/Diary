plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain:usecase"))
                implementation(project(":navigation:core"))
                implementation(project(":ui:compose"))
                implementation(project(":ui:decompose-compose"))
                implementation(project(":library:google-auth-compose"))
                implementation(project(":library:viewmodel"))

                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.account"
}
