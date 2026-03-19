import io.github.taetae98coding.diary.gradle.getLocalProperties

plugins {
    alias(libs.plugins.diary.primitive.android.application)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.dependency.guard)
}

private val localProperties = getLocalProperties()

android {
    namespace = BuildConfig.NAMESPACE

    defaultConfig {
        applicationId = "io.github.taetae98coding.diary"
        versionCode = BuildConfig.VERSION_CODE
        versionName = BuildConfig.VERSION_NAME
    }

    signingConfigs {
        create("dev") {
            storeFile = file("keystore/dev.jks")
            storePassword = localProperties.getProperty("dev.android.key.store.password")
            keyAlias = localProperties.getProperty("dev.android.key.alias")
            keyPassword = localProperties.getProperty("dev.android.key.password")
        }

        create("real") {
            storeFile = file("keystore/real.jks")
            storePassword = localProperties.getProperty("real.android.key.store.password")
            keyAlias = localProperties.getProperty("real.android.key.alias")
            keyPassword = localProperties.getProperty("real.android.key.password")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = null
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = null

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("dev")
        }
        create("real") {
            dimension = "environment"
            applicationIdSuffix = null
            signingConfig = signingConfigs.getByName("real")
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencyGuard {
    configuration("realReleaseRuntimeClasspath")
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(projects.app.shared)
    debugImplementation(libs.leakcanary)
}
