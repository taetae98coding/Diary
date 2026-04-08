plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.calendar)
                implementation(projects.domain.holiday)
                implementation(projects.domain.sync)
                implementation(projects.domain.memo)
                implementation(projects.domain.routine)
                implementation(projects.domain.tag)
                implementation(projects.domain.weather)
                implementation(projects.library.kotlinxCoroutinesCore)
                implementation(libs.androidx.paging.compose)
                implementation(libs.coil.compose)
            }
        }
    }
}
