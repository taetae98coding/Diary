plugins {
    id("diary.android.app")
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.taetae98.diary"

    defaultConfig {
        applicationId = "com.taetae98.diary"
        versionCode = 10000
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":app"))

    implementation(libs.android.material)
    implementation(libs.android.activity.compose)

    implementation(compose.material3)
}