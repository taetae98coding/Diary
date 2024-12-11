plugins {
    id("diary.kotlin.multiplatform.common")
}

kotlin {
    sourceSets {
        appleMain {
            dependencies{
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
