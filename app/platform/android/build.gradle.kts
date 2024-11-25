import ext.getLocalProperty

private val localProperties = requireNotNull(project.getLocalProperty())

plugins {
    id("diary.android.application")
    id("diary.kotlin.android")
    id("diary.compose")
    alias(libs.plugins.android.firebase.crashlytics)
    alias(libs.plugins.android.firebase.perf)
    alias(libs.plugins.google.services)
    alias(libs.plugins.dependency.guard)
    alias(libs.plugins.ksp)
}

android {
    namespace = Build.NAMESPACE

    signingConfigs {
        create("dev") {
            storeFile = file("keystore/dev.jks")
            storePassword = localProperties.getProperty("android.dev.store.password")
            keyAlias = localProperties.getProperty("android.dev.key.alias")
            keyPassword = localProperties.getProperty("android.dev.key.password")
        }

        create("real") {
            storeFile = file("keystore/real.jks")
            storePassword = localProperties.getProperty("android.real.store.password")
            keyAlias = localProperties.getProperty("android.real.key.alias")
            keyPassword = localProperties.getProperty("android.real.key.password")
        }
    }

    defaultConfig {
        applicationId = "io.github.taetae98coding.diary"

        versionCode = 4
        versionName = "1.2.1"
    }

    buildTypes {
        debug {
            isDefault = true

            applicationIdSuffix = ".debug"
            signingConfig = null
        }

        release {
            signingConfig = null

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    flavorDimensions.add("development")
    productFlavors {
        create("dev") {
            isDefault = true

            dimension = "development"
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("dev")

            buildConfigField("String", "DIARY_API_URL", "\"${localProperties.getProperty("diary.dev.api.base.url")}\"")
            buildConfigField("String", "HOLIDAY_API_URL", "\"${localProperties.getProperty("holiday.dev.api.url")}\"")
            buildConfigField("String", "HOLIDAY_API_KEY", "\"${localProperties.getProperty("holiday.dev.api.key")}\"")
        }

        create("real") {
            dimension = "development"
            signingConfig = signingConfigs.getByName("real")

            buildConfigField("String", "DIARY_API_URL", "\"${localProperties.getProperty("diary.real.api.base.url")}\"")
            buildConfigField("String", "HOLIDAY_API_URL", "\"${localProperties.getProperty("holiday.real.api.url")}\"")
            buildConfigField("String", "HOLIDAY_API_KEY", "\"${localProperties.getProperty("holiday.real.api.key")}\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":app:platform:common"))
    implementation(project(":app:core:diary-service"))
    implementation(project(":app:core:holiday-service"))
    implementation(project(":app:domain:fcm"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.startup)

    implementation(platform(libs.android.firebase.bom))
    implementation(libs.android.firebase.analytics)
    implementation(libs.android.firebase.crashlytics)
    implementation(libs.android.firebase.perf)
    implementation(libs.android.firebase.messaging)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(platform(libs.koin.annotations.bom))
    implementation(libs.koin.annotations)
    ksp(platform(libs.koin.annotations.bom))
    ksp(libs.koin.compiler)

    runtimeOnly(libs.ktor.client.okhttp)

    debugImplementation(libs.leakcanary)
}

dependencyGuard {
    configuration("realReleaseRuntimeClasspath") {
        allowedFilter = {
            !it.contains("junit") && !it.contains("leakcanary")
        }
    }
}
