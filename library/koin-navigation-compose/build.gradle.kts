plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:viewmodel"))

                implementation(compose.runtime)
                implementation(libs.decompose)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.koin.navigation.compose"
}
