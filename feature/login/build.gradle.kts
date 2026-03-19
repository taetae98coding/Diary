import io.github.taetae98coding.diary.gradle.nonAndroidMain

plugins {
    alias(libs.plugins.diary.convention.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.googleCredentials.compose)
                implementation(projects.domain.credentials)
            }
        }

        nonAndroidMain { }
    }
}
