plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.decompose)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.compose)

                api(project(":library:viewmodel"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.koin.navigation.compose"
}
