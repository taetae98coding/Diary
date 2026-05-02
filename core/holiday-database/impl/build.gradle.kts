import io.github.taetae98coding.diary.gradle.kspAll
import io.github.taetae98coding.diary.gradle.nonWasmMain

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.koin)
    alias(libs.plugins.room3)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.holidayDatabase.api)
                implementation(projects.library.kotlinxFile)
                implementation(projects.library.roomCommon)
                implementation(libs.androidx.room3.runtime)
            }
        }

        nonWasmMain {
            dependencies {
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(projects.library.sqliteWasmWorker)
                implementation(libs.androidx.sqlite.web)
            }
        }
    }
}

dependencies {
    kspAll(libs.androidx.room3.compiler)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}
