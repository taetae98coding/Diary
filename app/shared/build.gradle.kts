import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import io.github.taetae98coding.diary.gradle.getLocalProperties
import io.github.taetae98coding.diary.gradle.nonAndroidMain
import io.github.taetae98coding.diary.gradle.nonAppleMain

private val localProperties = getLocalProperties()

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.diary.primitive.koin)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.compose.core)
                implementation(projects.core.database.impl)
                implementation(projects.core.datastore.impl)
                implementation(projects.core.holidayDatabase.impl)
                implementation(projects.core.holidayNetwork.impl)
                implementation(projects.core.ipNetwork.impl)
                implementation(projects.core.location.impl)
                implementation(projects.core.navigation)
                implementation(projects.core.network.impl)
                implementation(projects.core.weatherNetwork.impl)
                implementation(projects.core.supabase.impl)
                implementation(projects.data.account)
                implementation(projects.data.credentials)
                implementation(projects.data.holiday)
                implementation(projects.data.memo)
                implementation(projects.data.routine)
                implementation(projects.data.sync)
                implementation(projects.data.tag)
                implementation(projects.data.weather)
                implementation(projects.domain.account)
                implementation(projects.domain.credentials)
                implementation(projects.domain.holiday)
                implementation(projects.domain.memo)
                implementation(projects.domain.routine)
                implementation(projects.domain.sync)
                implementation(projects.domain.tag)
                implementation(projects.domain.weather)
                implementation(projects.feature.calendar)
                implementation(projects.feature.login)
                implementation(projects.feature.memo)
                implementation(projects.feature.more)
                implementation(projects.feature.routine)
                implementation(projects.feature.tag)
                implementation(projects.library.navigation3Runtime)
                implementation(libs.jetbrains.compose.material3.navigation.suite)
                implementation(libs.jetbrains.navigation3.ui)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
                implementation(libs.jetbrains.lifecycle.viewmodel.navigation3)
                implementation(libs.koin.compose.viewmodel)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.lifecycle.process)
                runtimeOnly(libs.ktor.client.okhttp)
            }
        }

        jvmMain {
            dependencies {
                runtimeOnly(libs.kotlinx.coroutines.swing)
                runtimeOnly(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        wasmJsMain {
            dependencies {
                runtimeOnly(libs.ktor.client.js)
            }
        }

        nonAndroidMain {}
        nonAppleMain {}
    }
}

buildkonfig {
    packageName = "${BuildConfig.NAMESPACE}.app.shared"

    defaultConfigs {
    }

    defaultConfigs("dev") {
        buildConfigField(type = STRING, name = "SUPABASE_URL", value = requireNotNull(localProperties.getProperty("dev.supabase.url")), nullable = false, const = true)
        buildConfigField(type = STRING, name = "SUPABASE_KEY", value = requireNotNull(localProperties.getProperty("dev.supabase.key")), nullable = false, const = true)
        buildConfigField(type = STRING, name = "OPENWEATHERMAP_API_KEY", value = localProperties.getProperty("dev.openweathermap.api.key").orEmpty(), nullable = false, const = true)
    }

    defaultConfigs("real") {
        buildConfigField(type = STRING, name = "SUPABASE_URL", value = requireNotNull(localProperties.getProperty("real.supabase.url")), nullable = false, const = true)
        buildConfigField(type = STRING, name = "SUPABASE_KEY", value = requireNotNull(localProperties.getProperty("real.supabase.key")), nullable = false, const = true)
        buildConfigField(type = STRING, name = "OPENWEATHERMAP_API_KEY", value = localProperties.getProperty("real.openweathermap.api.key").orEmpty(), nullable = false, const = true)
    }

    targetConfigs("dev") {
        create("android") {
            buildConfigField(type = STRING, name = "GOOGLE_CLIENT_ID", value = requireNotNull(localProperties.getProperty("dev.web.google.client.id")), nullable = false, const = true)
        }
        create("jvm") {
            buildConfigField(type = STRING, name = "APP_NAME", value = "DiaryDev", nullable = false, const = true)
            buildConfigField(type = STRING, name = "GOOGLE_CLIENT_ID", value = requireNotNull(localProperties.getProperty("dev.desktop.google.client.id")), nullable = false, const = true)
        }
    }

    targetConfigs("real") {
        create("android") {
            buildConfigField(type = STRING, name = "GOOGLE_CLIENT_ID", value = requireNotNull(localProperties.getProperty("real.web.google.client.id")), nullable = false, const = true)
        }
        create("jvm") {
            buildConfigField(type = STRING, name = "APP_NAME", value = "Diary", nullable = false, const = true)
            buildConfigField(type = STRING, name = "GOOGLE_CLIENT_ID", value = requireNotNull(localProperties.getProperty("real.desktop.google.client.id")), nullable = false, const = true)
        }
    }
}
