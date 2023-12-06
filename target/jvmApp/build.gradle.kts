plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(project(":app"))

    implementation(compose.desktop.currentOs)
    implementation(compose.ui)
}

compose.desktop {
    application {
        mainClass = "com.taetae98.diary.MainKt"
    }
}
