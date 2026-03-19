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
    alias(libs.plugins.koin.compiler) apply false
    alias(libs.plugins.spm) apply false

    alias(libs.plugins.project.guard)
}

projectGuard {
    guard(":data") {
        deny(":feature")
        deny(":compose")
        deny(":presenter")
    }

    guard(":domain") {
        deny(":data")
        deny(":feature")
        deny(":compose")
        deny(":presenter")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:network")
        deny(":core:supabase")
        deny(":core:google-credentials")
    }

    guard(":feature") {
        deny(":data")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:network")
        deny(":core:supabase")
    }
    guard(":presenter") {
        deny(":data")
        deny(":core:database")
        deny(":core:datastore")
        deny(":core:network")
        deny(":core:supabase")
    }
}
