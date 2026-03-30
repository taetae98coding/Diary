plugins {
    // kotlin
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // agp
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    // jetbrains
    alias(libs.plugins.jetbrains.compose) apply false
    // abc
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.dependency.guard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.koin.compiler) apply false
    alias(libs.plugins.spm) apply false

    alias(libs.plugins.project.guard)
}

projectGuard {
    guard(":data") {
        deny(":feature")
        deny(":compose")
        deny(":presenter")
        deny(":core:navigation")
    }

    guard(":domain") {
        deny(":data")
        deny(":feature")
        deny(":compose")
        deny(":presenter")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:google-credentials")
        deny(":core:holiday-database")
        deny(":core:holiday-network")
        deny(":core:ip-network")
        deny(":core:mapper")
        deny(":core:navigation")
        deny(":core:network")
        deny(":core:supabase")
        deny(":core:weather-network")
    }

    guard(":feature") {
        deny(":data")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:holiday-database")
        deny(":core:holiday-network")
        deny(":core:ip-network")
        deny(":core:mapper")
        deny(":core:network")
        deny(":core:supabase")
        deny(":core:weather-network")
    }

    guard(":presenter") {
        deny(":data")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:holiday-database")
        deny(":core:holiday-network")
        deny(":core:ip-network")
        deny(":core:mapper")
        deny(":core:network")
        deny(":core:supabase")
        deny(":core:weather-network")
    }
}
