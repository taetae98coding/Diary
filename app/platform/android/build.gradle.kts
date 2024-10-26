import java.util.Properties

private val localProperties = requireNotNull(project.getLocalProperty())

public fun Project.getLocalProperty(): Properties? {
    val file = project.rootProject.file("local.properties")

    return if (file.exists()) {
        Properties().apply {
            file.inputStream()
                .buffered()
                .use { load(it) }
        }
    } else {
        null
    }
}

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dependency.guard)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

android {
    namespace = "io.github.taetae98coding.diary"

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

        compileSdk = 35
        minSdk = 33
        targetSdk = 35

        versionCode = 1
        versionName = "1.0.0"
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
        }

        create("real") {
            dimension = "development"
            signingConfig = signingConfigs.getByName("real")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":app:platform:common"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.leakcanary)
}

dependencyGuard {
    configuration("realReleaseRuntimeClasspath") {
        allowedFilter = {
            !it.contains("junit") && !it.contains("leakcanary")
        }
    }
}
