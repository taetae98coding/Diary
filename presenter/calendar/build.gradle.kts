plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.library.kotlinxCoroutinesCore)
                implementation(projects.domain.holiday)
                api(projects.core.model)
                api(libs.kotlinx.datetime)

                implementation(projects.compose.core)
                implementation(projects.compose.calendar)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
