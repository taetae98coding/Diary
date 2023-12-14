import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.android.app")
    id("diary.compose.multiplatform")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.taetae98.diary"

    defaultConfig {
        applicationId = "com.taetae98.diary"
        versionCode = 10000
        versionName = "1.0.0"
    }

    signingConfigs {
        create("debugSigning") {
            storeFile = file("keystore/debug.jks")
            storePassword = getLocalProperty("key.debug.store.password")
            keyAlias = getLocalProperty("key.debug.alias")
            keyPassword = getLocalProperty("key.debug.password")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debugSigning")
        }

        release {
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

    implementation(compose.ui)

    implementation(libs.decompose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}