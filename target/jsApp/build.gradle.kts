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
                implementation(project(":navigation:core"))

                implementation(compose.ui)

                implementation(libs.decompose)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
            }
        }
    }
}

compose.experimental {
    web.application
}