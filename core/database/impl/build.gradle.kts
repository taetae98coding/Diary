import com.android.build.api.variant.HasHostTests
import io.github.taetae98coding.diary.gradle.kspAll
import io.github.taetae98coding.diary.gradle.nonWasmMain

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.android.test)
    alias(libs.plugins.diary.primitive.koin)
    alias(libs.plugins.room3)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.database.api)
                implementation(projects.library.roomCommon)
                implementation(projects.library.kotlinxFile)
                implementation(libs.androidx.room3.runtime)
                implementation(libs.androidx.room3.paging)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        nonWasmMain {
            dependencies {
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(libs.androidx.sqlite.web)
                implementation(projects.library.sqliteWasmWorker)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.androidx.room3.testing)
            }
        }
    }
}

androidComponents {
    onVariants { variant ->
        (variant as? HasHostTests)
            ?.hostTests
            ?.values
            ?.forEach { hostTest ->
                hostTest.sources.assets?.addStaticSourceDirectory("$projectDir/schemas")
            }
    }
}

dependencies {
    kspAll(libs.androidx.room3.compiler)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}
