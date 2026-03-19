plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.koin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.supabase.api)
                implementation(libs.supabase.auth)
                implementation(libs.supabase.functions)
            }
        }
    }
}
