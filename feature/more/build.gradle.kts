plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.calendar)
                implementation(projects.domain.account)
                implementation(projects.domain.credentials)
                implementation(projects.domain.holiday)
                implementation(projects.library.kotlinxDatetime)
            }
        }
    }
}
