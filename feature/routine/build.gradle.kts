plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.routine)
                implementation(libs.androidx.paging.compose)
            }
        }
    }
}
