plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.memo)
                implementation(projects.domain.sync)
                implementation(projects.domain.tag)
                implementation(projects.presenter.memo)
                implementation(projects.presenter.tag)
            }
        }
    }
}
