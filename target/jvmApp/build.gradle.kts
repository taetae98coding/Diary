plugins {
    id("diary.jvm")
    alias(libs.plugins.compose.multiplatform)
}

dependencies {
    implementation(project(":app"))
    implementation(project(":navigation:core"))

    implementation(compose.desktop.currentOs)
    implementation(compose.ui)

    implementation(libs.decompose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.kotlinx.coroutines.swing)
}

compose.desktop {
    application {
        mainClass = "com.taetae98.diary.MainKt"
    }
}
