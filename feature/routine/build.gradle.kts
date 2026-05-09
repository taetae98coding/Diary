plugins {
    alias(libs.plugins.diary.convention.feature)
    alias(libs.plugins.diary.primitive.android.compose.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.domain.routine)
                implementation(projects.domain.sync)
                implementation(libs.androidx.paging.compose)
            }
        }
    }
}
