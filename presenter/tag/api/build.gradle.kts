plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.navigation)
                api(projects.core.model)
                api(libs.kotlinx.coroutines.core)
                api(libs.androidx.paging.common)
            }
        }
    }
}
