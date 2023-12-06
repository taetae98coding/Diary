plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

android {
    namespace = "com.taetae98.diary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.taetae98.diary"
        minSdk = 32
        targetSdk = 34
        versionCode = 10000
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(libs.android.material)
    implementation(libs.android.activity.compose)

    implementation(compose.material3)
}