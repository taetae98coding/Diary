plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.core)
                implementation(projects.compose.calendar)
                implementation(projects.domain.holiday)
                implementation(projects.domain.weather)
                implementation(projects.library.kotlinxCoroutinesCore)
                api(projects.core.model)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.coil.compose)
                api(libs.kotlinx.datetime)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.compose.ui.tooling)
}
