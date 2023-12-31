plugins {
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.cocoapods).apply(false)

    alias(libs.plugins.kotlinx.serialization).apply(false)

    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)

    alias(libs.plugins.compose.multiplatform).apply(false)

    alias(libs.plugins.google.services).apply(false)

    alias(libs.plugins.firebase.crashlytics).apply(false)
    alias(libs.plugins.firebase.performance).apply(false)

    alias(libs.plugins.sqldelight).apply(false)

    alias(libs.plugins.kotest).apply(false)

    alias(libs.plugins.buildkonfig).apply(false)
    alias(libs.plugins.ksp).apply(false)
}
