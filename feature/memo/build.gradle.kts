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
                implementation(project(":domain:usecase"))

                implementation(project(":library:koin-navigation-compose"))
                implementation(project(":library:compose-runtime"))
                implementation(project(":library:uuid"))
                implementation(project(":library:paging"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
                implementation(libs.paging.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.memo"
}
