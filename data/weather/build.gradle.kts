plugins {
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.ipNetwork.api)
                implementation(projects.core.weatherNetwork.api)
                implementation(projects.domain.weather)
            }
        }
    }
}
