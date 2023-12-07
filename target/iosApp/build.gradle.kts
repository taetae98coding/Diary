plugins {
    id("diary.ios")
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    cocoapods {
        version = "1.0.0"
        homepage = "homepage"
        summary = "summary"

        framework {
            baseName = "iosApp"
            isStatic = true
        }
    }

    sourceSets {
        iosMain {
            dependencies {
                implementation(project(":app"))
                implementation(compose.ui)
            }
        }
    }
}
