plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.memo)
                implementation(projects.domain.sync)
                implementation(projects.presenter.calendar.compose)
            }
        }
    }
}
