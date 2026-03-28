plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.sync)
                implementation(projects.domain.tag)
                implementation(projects.presenter.tag.compose)
            }
        }
    }
}
