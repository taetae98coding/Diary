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
                implementation(project(":library:compose-runtime"))
                implementation(project(":library:google-sign-compose"))
                implementation(project(":library:viewmodel"))
                implementation(project(":library:koin-navigation-compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.account"
}
