plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.napier)
                api(projects.logger.console.api)
            }
        }
    }
}
