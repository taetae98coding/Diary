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
                implementation(libs.androidx.room3.runtime)
                implementation(libs.androidx.room3.paging)
            }
        }

        nonWasmMain {
            dependencies {
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.androidx.room3.testing)
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
