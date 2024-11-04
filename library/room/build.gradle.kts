plugins {
    id("diary.room")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:datetime"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.room"
}
