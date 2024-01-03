plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":navigation:core"))
                implementation(project(":domain:usecase"))

                implementation(project(":ui:compose"))

                implementation(project(":library:compose-runtime"))
                implementation(project(":library:compose-calendar"))
                implementation(project(":library:viewmodel"))
                implementation(project(":library:koin-navigation-compose"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.immutable)

                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.feature.calendar"
}
