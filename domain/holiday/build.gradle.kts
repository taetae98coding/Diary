plugins {
    alias(libs.plugins.diary.convention.domain)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.library.kotlinxDatetime)
            }
        }
    }
}
