plugins {
    id("diary.ios")
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "Diary"
        homepage = "https://github.com/taetae98coding/Diary"

        framework {
            baseName = "iosApp"
            isStatic = true

            export(project(":navigation:core"))
            export(libs.decompose)
            export(libs.decompose.lifecycle)
        }
    }

    sourceSets {
        iosMain {
            dependencies {
                implementation(project(":app"))
                implementation(compose.ui)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                api(project(":navigation:core"))
                api(libs.decompose)
                api(libs.decompose.lifecycle)
            }
        }
    }
}
