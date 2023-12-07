plugins {
    id("diary.jvm")
    alias(libs.plugins.compose.multiplatform)
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
