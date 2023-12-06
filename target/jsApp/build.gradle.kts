plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    explicitApi()
    jvmToolchain(17)

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(project(":app"))

                implementation(compose.ui)
            }
        }
    }
}

compose.experimental {
    web.application
}