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
                implementation(libs.jetbrains.compose.ui)
            }
        }
    }
}
