import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.diary.primitive.wasm)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    wasmJs {
        useEsModules()
    }

    sourceSets {
        wasmJsMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(libs.androidx.sqlite.web)
                implementation(npm("sqlite-wasm-worker", project.layout.projectDirectory.dir("worker").asFile))
            }
        }
    }
}
