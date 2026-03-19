plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.account)
                implementation(projects.domain.credentials)
            }
        }
    }
}
