plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.memo)
                implementation(projects.domain.tag)
                implementation(projects.presenter.memo.compose)
            }
        }
    }
}
