import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.android.app")
    id("diary.compose.multiplatform")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.taetae98.debug"

    defaultConfig {
        applicationId = "com.taetae98.debug"
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        create("debugSign") {
            storeFile = file("keystore/release.jks")
            storePassword = getLocalProperty("key.debug.app.store.password")
            keyAlias = getLocalProperty("key.debug.app.alias")
            keyPassword = getLocalProperty("key.debug.app.password")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debugSign")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(compose.material3)

    implementation(project.dependencies.platform(libs.firebase.android.bom))
    implementation(libs.firebase.android.firestore)
}