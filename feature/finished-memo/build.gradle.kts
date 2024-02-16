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
                implementation(project(":ui:memo-compose"))
                implementation(project(":library:koin-navigation-compose"))

                implementation(compose.material3)
                implementation(libs.decompose.compose)
                implementation(libs.paging.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.finished.memo"
}
