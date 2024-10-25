import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    explicitApi()
    jvmToolchain(17)

    wasmJs {
        browser()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        wasmJsMain {
            dependencies {
                implementation(compose.material3)
            }
        }
    }
}
