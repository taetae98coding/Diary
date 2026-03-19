plugins {
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.database.api)
                implementation(projects.core.network.api)
                implementation(projects.domain.sync)
            }
        }
    }
}
