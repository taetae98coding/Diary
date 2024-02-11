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

                implementation(project(":library:viewmodel"))
                implementation(project(":library:compose-runtime"))
                implementation(project(":library:koin-navigation-compose"))
                implementation(project(":library:uuid"))
                implementation(project(":library:paging"))

                implementation(project(":ui:compose"))
                implementation(project(":ui:entity"))
                implementation(project(":ui:memo-compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
                implementation(libs.paging.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.tag"
}
