plugins {
    id("diary.kotlin.multiplatform.common")
    id("diary.koin.common")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:model"))
                implementation(libs.bundles.ktor.client)
            }
        }
    }
}
