plugins {
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.holidayDatabase.api)
                implementation(projects.core.holidayNetwork.api)
                implementation(projects.domain.holiday)
            }
        }
    }
}
