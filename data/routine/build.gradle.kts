plugins {
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.database.api)
                implementation(projects.core.datastore.api)
                implementation(projects.domain.routine)
            }
        }
    }
}
