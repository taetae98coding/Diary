plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":ui:compose"))
                implementation(project(":library:compose-color"))
                implementation(project(":library:kotlin-ext"))

                implementation(libs.kotlinx.datetime)
                implementation(compose.material3)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.uiTooling)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.entity"
}
