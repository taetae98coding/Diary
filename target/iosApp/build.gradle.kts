plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    explicitApi()
    jvmToolchain(17)

    iosArm64()
    iosSimulatorArm64()

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
