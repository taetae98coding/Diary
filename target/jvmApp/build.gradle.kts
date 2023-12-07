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
}

compose.desktop {
    application {
        mainClass = "com.taetae98.diary.MainKt"
    }
}
