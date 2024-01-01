plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.immutable)

                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.calendar.compose"
}
