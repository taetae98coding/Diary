plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:compose-color"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.immutable)

                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.compose.calendar"
}
