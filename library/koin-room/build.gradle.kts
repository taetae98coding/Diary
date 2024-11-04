plugins {
    id("diary.room")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.koin.room"
}
