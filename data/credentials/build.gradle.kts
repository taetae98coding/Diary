plugins {
    alias(libs.plugins.diary.convention.data)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.datastore.api)
                implementation(projects.core.network.api)
                implementation(projects.core.supabase.api)
                implementation(projects.domain.credentials)
            }
        }
    }
}
