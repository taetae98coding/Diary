plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.holiday)
                implementation(projects.domain.sync)
                implementation(projects.domain.memo)
                implementation(projects.domain.tag)
                implementation(projects.presenter.calendar)
                implementation(libs.androidx.paging.compose)
            }
        }
    }
}
