plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.diary.primitive.koin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.supabase.api)
                api(projects.core.network.api)
            }
        }
    }
}
