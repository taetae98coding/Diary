plugins {
    id("diary.js")
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    js(IR) {
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