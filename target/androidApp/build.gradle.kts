import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.android.app")
    id("diary.compose.multiplatform")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
}

android {
    namespace = "com.taetae98.diary"

    defaultConfig {
        applicationId = "com.taetae98.diary"
        versionCode = 11
        versionName = "1.1.1"
    }

    signingConfigs {
        create("debugSigning") {
            storeFile = file("keystore/debug.jks")
            storePassword = getLocalProperty("key.debug.store.password")
            keyAlias = getLocalProperty("key.debug.alias")
            keyPassword = getLocalProperty("key.debug.password")
        }

        create("releaseSigning") {
            storeFile = file("keystore/release.jks")
            storePassword = getLocalProperty("key.release.store.password")
            keyAlias = getLocalProperty("key.release.alias")
            keyPassword = getLocalProperty("key.release.password")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debugSigning")
        }

        release {
            signingConfig = signingConfigs.getByName("releaseSigning")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":app"))
    implementation(project(":navigation:core"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.process)

    implementation(compose.ui)

    implementation(libs.decompose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(platform(libs.firebase.android.bom))
    implementation(libs.firebase.android.analytics)
    implementation(libs.firebase.android.crashlytics)
    implementation(libs.firebase.android.performance)
}