plugins {
    id("diary.kotlin.multiplatform.common")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":library:datetime"))
                implementation(libs.kotlinx.serialization.core)
            }
        }
    }
}
