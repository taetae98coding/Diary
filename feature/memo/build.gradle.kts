plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    explicitApi()
    jvmToolchain(17)

    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    js(IR) {
        browser()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "com.taetae98.diary.feature.memo"

    compileSdk = 34

    defaultConfig {
        minSdk = 32
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}
