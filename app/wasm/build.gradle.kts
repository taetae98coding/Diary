plugins {
    alias(libs.plugins.diary.primitive.wasm)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "diary.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.shared)
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.jetbrains.compose.material3)
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "io.github.taetae98coding.diary"
    generateResClass = always
}
