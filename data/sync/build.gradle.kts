import io.github.taetae98coding.diary.gradle.nonAndroidMain

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.database.api)
                implementation(projects.core.network.api)
                implementation(projects.domain.account)
                implementation(projects.domain.sync)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.work.runtime)
            }
        }

        nonAndroidMain {}
    }
}
